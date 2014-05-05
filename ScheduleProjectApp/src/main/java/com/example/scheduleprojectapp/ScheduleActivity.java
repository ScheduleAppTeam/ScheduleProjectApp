package com.example.scheduleprojectapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;

import static com.example.scheduleprojectapp.NecessaryFunctions.GetColorOfTheWeek;
import static com.example.scheduleprojectapp.NecessaryFunctions.isOnline;

public class ScheduleActivity extends Activity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    static ViewPager mViewPager;
    private int currentDate;
    public static DiapasonOfDays days;

    public static DayOfWeek[] dayWithDates;

    public static Context context;
    public static ScheduleLesson[] ScheduleSet;
    public static ArrayList<Comment> CommentSet;
    private ScheduleRefreshing refreshScheduleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        context = getApplicationContext();
        days = new DiapasonOfDays();
        dayWithDates = new DayOfWeek[Constants.DIAPASON_OF_DAYS];
        CommentSet = new ArrayList<Comment>();

        ScheduleSet = new ScheduleLesson[Constants.NUM_OF_LESSONS_IN_DAY];

        MakeSchedule();
        MakeComments();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        GregorianCalendar gg = new GregorianCalendar();
        Constants.WEEK_COLOR weekColor = GetColorOfTheWeek(gg.get(Calendar.WEEK_OF_YEAR));
        int dayInWeek = gg.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < dayWithDates.length; i++){
            if ((dayWithDates[i].GetColorOfDay() == weekColor) && (dayWithDates[i].GetNumOfDayInWeek() == dayInWeek)){
                currentDate = (i - 1);
                mViewPager.setCurrentItem(currentDate);
                break;
            }
        }
    }

    public void MakeComments(){
        GregorianCalendar greg = new GregorianCalendar();
        CommentTableDatabaseHelper db = new CommentTableDatabaseHelper(context);

        Log.d(Constants.LOG_TAG, "Day: " + String.valueOf(greg.get(Calendar.DAY_OF_YEAR)));
        CommentSet = db.getComments(greg.get(Calendar.DAY_OF_YEAR));
    }

    public DayOfWeek ConstructConcreteDayV2(int pos){
        GregorianCalendar goodGreg = new GregorianCalendar();
        goodGreg.add(Calendar.DAY_OF_YEAR, pos);

        ScheduleTableDataBaseHelper db = new ScheduleTableDataBaseHelper(context);
        List<Schedule_Lesson> list = db.getLesson(goodGreg.get(Calendar.DAY_OF_WEEK), GetColorOfTheWeek(goodGreg.get(Calendar.WEEK_OF_YEAR)).ordinal());

        if (list.size() > 0){
            for (Schedule_Lesson item : list){
                Log.i(Constants.LOG_TAG, item.GetLessonName());
            }
        }else{
            Log.i(Constants.LOG_TAG, "list is empty");
        }

        DayOfWeek day = new DayOfWeek(goodGreg.get(Calendar.DAY_OF_YEAR),
                (goodGreg.get(Calendar.DAY_OF_WEEK) - 1),
                goodGreg.get(Calendar.DAY_OF_MONTH),
                goodGreg.get(Calendar.MONTH),
                MakeLessonsList(list),
                GetColorOfTheWeek(goodGreg.get(Calendar.WEEK_OF_YEAR)));
        return day;
    }

    private Schedule_Lesson[] MakeLessonsList(List<Schedule_Lesson> lessonList){
        VenueTableDataBaseHelper db = new VenueTableDataBaseHelper(getApplicationContext());

        List<Subject> list = new ArrayList<Subject>();
        for (Schedule_Lesson item : lessonList){
            Venue venue = db.getVenue(item.GetVenue());
            list.add(new Subject(item.GetLessonName(), NecessaryFunctions.MakeLessonInfo(item.GetLessonType(), venue), item.GetTeacher(), item.GetLessonNum()));
        }

        return list.toArray(new Schedule_Lesson[list.size()]);
    }

    public void MakeSchedule(){
        for (int i = Constants.DAYS_DEPTH, j = 0; i < Constants.DIAPASON_OF_DAYS + Constants.DAYS_DEPTH; i++, j++){
            dayWithDates[j] = ConstructConcreteDayV2(i);
            Log.i(Constants.LOG_TAG, "Create a " + String.valueOf(dayWithDates[j].dayInMonth) + " day in month");
        }

        days.SetDiapason(dayWithDates);
    }

    public void LeftSwipeButtonClicked(View v){
        setTheme(android.R.style.Theme_Holo_Light);
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
    }

    public void RightSwipeButtonClicked(View v){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_activity:
                Log.i(Constants.LOG_TAG, "settings button clicked");
                if (isOnline(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(context, "Нет интернет соединения", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.refresh_activity:
                RefreshSchedule();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void OnDateButtonClick(View v){
        Log.i(Constants.LOG_TAG, "on date button clicked");
        mViewPager.setCurrentItem(currentDate, false);
    }

    public void AddCommentButtonClick(View v){
        Log.i(Constants.LOG_TAG, "comment block constructor activated");

        try{
            EditText text = (EditText)findViewById(R.id.CommentTextValue);

            FormChecker.isFilled(text, "Введите комментарий");
            Toast.makeText(context, "Комментарий добавлен", Toast.LENGTH_LONG).show();

            text.setText("");
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void RefreshSchedule(){
        if (refreshScheduleTask == null) {
            refreshScheduleTask = new ScheduleRefreshing(this);
            refreshScheduleTask.execute();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return Constants.DIAPASON_OF_DAYS;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static Integer sectionNum;

        public static PlaceholderFragment newInstance(int sectionNumber) {

            Log.i(Constants.LOG_TAG, "created" + String.valueOf(sectionNumber));
            sectionNum = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public void MakeScheduleView(View rootView, Bundle args){
            DayOfWeek day;

            try{
                day = days.GetDiapason()[args.getInt(ARG_SECTION_NUMBER) - 1];
            }catch (Exception ex){
                day = days.GetDiapason()[0];
                Log.i(Constants.LOG_TAG, "mistake in page position");
            }

            TextView weekDay = (TextView) rootView.findViewById(R.id.DayOfWeekET);

            try {
                weekDay.setText(NecessaryFunctions.GetDay(day.GetNumOfDayInWeek()));
            }catch (Exception ex){
                Log.i(Constants.LOG_TAG, "illegal day of week");
            }

            TextView month = (TextView) rootView.findViewById(R.id.DateET);

            try {
                month.setText(NecessaryFunctions.GetMonth(day.GetMonth()) + ", " + day.GetDayInMonth());
            }catch (Exception ex){
                Log.i(Constants.LOG_TAG, "illegal month");
            }

            //Week color
            ImageView weekColor = (ImageView) rootView.findViewById(R.id.WeekColorLine);

            if (day.GetColorOfDay() == Constants.WEEK_COLOR.RED){
                weekColor.setImageResource(R.drawable.ic_red_week_line);
            }else{
                weekColor.setImageResource(R.drawable.ic_blue_week_line);
            }

            Schedule_Lesson[] subjs = day.GetSubjects();

            /*for (int i = 0; i < subjs.length; i++){
                ScheduleLesson lesson;

                try{
                    lesson = GetLessonET(subjs[i].GetPositionOfLesson(), rootView);

                    lesson.GetLesson().setText(subjs[i].GetLesson());
                    lesson.GetCabinet().setText(subjs[i].GetCabinet());
                    lesson.GetTeacher().setText(subjs[i].GetTeacher());
                }catch (Exception ex){
                    Log.i(Constants.LogTag, ex.getMessage() + "  " + i);
                }
            }*/
        }

        public void MakeCommentBlock(View rootView, Bundle args){
            CommentTableDatabaseHelper db = new CommentTableDatabaseHelper(context);

            List<Comment> commentSet = db.getComments(dayWithDates[sectionNum].GetDayInYear());
            if (commentSet.size() > 0){
                for (Comment item : commentSet){
                    TableLayout tbl = (TableLayout) rootView.findViewById(R.id.CommentBlock);

                    LinearLayout ll = new LinearLayout(context);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setPadding(0, 0, 0, 20);
                    TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                    paramsExample.weight = 1;

                    TextView person = new TextView(context);
                    person.setText(item.GetFname() + " " + item.GetSname());
                    person.setLayoutParams(paramsExample);
                    person.setTextColor(getResources().getColor(R.color.TextColor));

                    TextView date = new TextView(context);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(item.GetDataComment()));
                    calendar.set(Calendar.MILLISECOND, 0);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss.SSS'Z'");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

                    DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    try {
                        date.setText(sdf.format(calendar.getTime()));
                    } catch (Exception e) {
                        Log.e(Constants.LOG_TAG, "incorrect month");
                    }

                    date.setLayoutParams(paramsExample);
                    date.setTextColor(getResources().getColor(R.color.TextColor));

                    TextView text = new TextView(context);
                    text.setText(item.GetText());
                    text.setLayoutParams(paramsExample);
                    text.setTextColor(getResources().getColor(R.color.TextColor));

                    ll.addView(person);
                    ll.addView(date);
                    ll.addView(text);

                    tbl.addView(ll);
                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            Bundle args = getArguments();
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

            MakeScheduleView(rootView, args);
            MakeCommentBlock(rootView, args);

            return rootView;
        }

        public ScheduleLesson GetLessonET(int position, View rootView) throws Exception{
            ScheduleLesson lesson = new ScheduleLesson();

            switch (position){
                case 1:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.FirstSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.FirstCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.FirstTeacherET));
                    break;
                case 2:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.SecondSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.SecondCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.SecondTeacherET));
                    break;
                case 3:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.ThirdSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.ThirdCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.ThirdTeacherET));
                    break;
                case 4:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.FourthSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.FourthCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.FourthTeacherET));
                    break;
                case 5:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.FifthSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.FifthCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.FifthTeacherET));
                    break;
                case 6:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.SixthSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.SixthCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.SixthTeacherET));
                    break;
                case 7:
                    lesson.SetLesson((TextView) rootView.findViewById(R.id.SeventhSubjectET));
                    lesson.SetCabinet((TextView) rootView.findViewById(R.id.SeventhCabinetET));
                    lesson.SetTeacher((TextView) rootView.findViewById(R.id.SeventhTeacherET));
                    break;
                default:
                    throw new Exception("mistake");
            }

            return lesson;
        }
    }

    public class ScheduleRefreshing extends AsyncTask<String, Integer, Boolean> implements
            DialogInterface.OnCancelListener {
        private Context context;
        private final Integer numberOfSteps = 30;
        private final Integer delay = 100;

        ScheduleRefreshing(Activity activity) {
            context = activity.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(final String... strings) {
            for (int i = 0; i < numberOfSteps; i++) {
                try {
                    Thread.sleep(delay);

                } catch (Exception e) {
                    Toast.makeText(context, "WTF", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected final void onPostExecute(final Boolean result) {
            if (result){
                Toast.makeText(context, "Расписание обновлено", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        @Override
        public final void onCancel(final DialogInterface dialog)
        {
            this.cancel(true);
        }
    }
}
