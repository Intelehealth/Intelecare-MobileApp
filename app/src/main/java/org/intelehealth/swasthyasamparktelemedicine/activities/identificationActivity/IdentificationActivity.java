package org.intelehealth.swasthyasamparktelemedicine.activities.identificationActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import org.intelehealth.swasthyasamparktelemedicine.BuildConfig;
import org.intelehealth.swasthyasamparktelemedicine.R;
import org.intelehealth.swasthyasamparktelemedicine.activities.cameraActivity.CameraActivity;
import org.intelehealth.swasthyasamparktelemedicine.activities.homeActivity.HomeActivity;
import org.intelehealth.swasthyasamparktelemedicine.activities.patientDetailActivity.PatientDetailActivity;
import org.intelehealth.swasthyasamparktelemedicine.activities.privacyNoticeActivity.PrivacyNotice_Activity;
import org.intelehealth.swasthyasamparktelemedicine.activities.searchPatientActivity.SearchPatientActivity;
import org.intelehealth.swasthyasamparktelemedicine.activities.setupActivity.SetupActivity;
import org.intelehealth.swasthyasamparktelemedicine.app.AppConstants;
import org.intelehealth.swasthyasamparktelemedicine.app.IntelehealthApplication;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.EncounterDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.ImagesDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.ImagesPushDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.ObsDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.PatientsDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.SyncDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.VisitAttributeListDAO;
import org.intelehealth.swasthyasamparktelemedicine.database.dao.VisitsDAO;
import org.intelehealth.swasthyasamparktelemedicine.knowledgeEngine.Node;
import org.intelehealth.swasthyasamparktelemedicine.models.ClsUserGetResponse;
import org.intelehealth.swasthyasamparktelemedicine.models.GetPassword;
import org.intelehealth.swasthyasamparktelemedicine.models.GetUserCallRes.UserCallRes;
import org.intelehealth.swasthyasamparktelemedicine.models.NewUserCreationCall.NameUser;
import org.intelehealth.swasthyasamparktelemedicine.models.NewUserCreationCall.PersonUser;
import org.intelehealth.swasthyasamparktelemedicine.models.NewUserCreationCall.UserCreationData;
import org.intelehealth.swasthyasamparktelemedicine.models.Patient;
import org.intelehealth.swasthyasamparktelemedicine.models.ResultsItem;
import org.intelehealth.swasthyasamparktelemedicine.models.dto.EncounterDTO;
import org.intelehealth.swasthyasamparktelemedicine.models.dto.ObsDTO;
import org.intelehealth.swasthyasamparktelemedicine.models.dto.PatientAttributesDTO;
import org.intelehealth.swasthyasamparktelemedicine.models.dto.PatientDTO;
import org.intelehealth.swasthyasamparktelemedicine.models.dto.VisitDTO;
import org.intelehealth.swasthyasamparktelemedicine.models.loginModel.LoginModel;
import org.intelehealth.swasthyasamparktelemedicine.syncModule.SyncUtils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.Base64Utils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.DateAndTimeUtils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.EditTextUtils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.FileUtils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.Logger;
import org.intelehealth.swasthyasamparktelemedicine.utilities.NetworkConnection;
import org.intelehealth.swasthyasamparktelemedicine.utilities.SessionManager;
import org.intelehealth.swasthyasamparktelemedicine.utilities.StringUtils;
import org.intelehealth.swasthyasamparktelemedicine.utilities.UrlModifiers;
import org.intelehealth.swasthyasamparktelemedicine.utilities.UuidDictionary;
import org.intelehealth.swasthyasamparktelemedicine.utilities.UuidGenerator;
import org.intelehealth.swasthyasamparktelemedicine.utilities.exception.DAOException;
import org.intelehealth.swasthyasamparktelemedicine.widget.materialprogressbar.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

//import static org.intelehealth.ekalhelpline.utilities.StringUtils.en__as_dob;

public class IdentificationActivity extends AppCompatActivity {
    private static final String TAG = IdentificationActivity.class.getSimpleName();
    private static final String EXTRA_MEDICAL_ADVICE = "EXTRA_MEDICAL_ADVICE";
    SessionManager sessionManager = null;
    private boolean hasLicense = false;
    private ArrayAdapter<CharSequence> educationAdapter;
    private ArrayAdapter<CharSequence> casteAdapter;
    private ArrayAdapter<CharSequence> economicStatusAdapter;
    UuidGenerator uuidGenerator = new UuidGenerator();
    Calendar today = Calendar.getInstance();
    Calendar dob = Calendar.getInstance();
    Patient patient1 = new Patient();
    private String patientUuid = "";
    private String mGender;
    String patientID_edit;
    private int mDOBYear;
    private int mDOBMonth;
    private int mDOBDay;
    private DatePickerDialog mDOBPicker;
    private int mAgeYears = 0;
    private int mAgeMonths = 0;
    private int mTimeHours = 0;
    private int mTimeMins = 0;
    private int mAgeDays = 0;
    private String country1, state;
    PatientsDAO patientsDAO = new PatientsDAO();
    EditText mFirstName;
    EditText mMiddleName;
    EditText mLastName;
    EditText mDOB;
    EditText mPhoneNum;
    EditText mAge;
    MaterialAlertDialogBuilder mAgePicker;
    MaterialAlertDialogBuilder mTimePicker;
    EditText mAddress1;
    EditText mAddress2;
    AutoCompleteTextView mCity;
    EditText mPostal;
    RadioButton mGenderM;
    RadioButton mGenderF;
    EditText mRelationship;
    //  EditText mOccupation;
    EditText countryText;
    //    EditText stateText;
    AutoCompleteTextView autocompleteState;
    EditText casteText;
    Spinner mCountry;
    //    Spinner mState;
    EditText economicText;
    EditText educationText;
    TextInputLayout casteLayout;
    TextInputLayout economicLayout;
    TextInputLayout educationLayout;
    LinearLayout countryStateLayout;
    Spinner mCaste;
    Spinner mEducation;
    Spinner mEconomicStatus;
    ImageView mImageView;
    String uuid = "";
    PatientDTO patientdto = new PatientDTO();
    ImagesDAO imagesDAO = new ImagesDAO();
    private String mCurrentPhotoPath;
    Context context;
    private String BlockCharacterSet_Others = "0123456789\\@$!=><&^*+€¥£`~";
    private String BlockCharacterSet_Name = "\\@$!=><&^*+\"\'€¥£`~";

    Intent i_privacy;
    String privacy_value;
    private int retainPickerYear;
    private int retainPickerMonth;
    private int retainPickerDate;
    Spinner occupation_spinner, bankaccount_spinner, mobilephone_spinner, whatsapp_spinner,
            source_of_water_spinner, howtomake_water_safe_spinner, water_availability_spinner,
            toilet_facility_spinner, structure_of_house_spinner;
    MaterialCheckBox familyhead_checkbox, time_water_checkbox, hectars_land_checkbox;
    EditText time_water_editText, hectars_land_editText, no_of_member_edittext, no_of_staying_members_edittext,
            occupation_edittext, watersafe_edittext, toiletfacility_edittext;
    CardView cardview_household;
    ArrayAdapter<CharSequence> occupation_adapt, bankaccount_adapt, mobile_adapt, whatsapp_adapt,
            sourcewater_adapt, watersafe_adapt, availa_adapt, toiletfacility_adapt, structure_adapt;
    String occupation_edittext_value = "", watersafe_edittext_value = "", toilet_edittext_value = "";
    int dob_indexValue = 15;
    String encoded = null;
    Base64Utils base64Utils = new Base64Utils();
    private String personUUID = "";
    //random value assigned to check while editing. If user didnt updated the dob and just clicked on fab
    //in that case, the edit() will get the dob_indexValue as 15 and we  will check if the
    //dob_indexValue == 15 then just get the mDOB editText value and add in the db.
    private Spinner state_spinner, city_spinner, block_spinner;
    private EditText et_tested_positive_date;
    CustomProgressDialog cpd;
    private TextWatcher mobileNumberWatcher;
    private boolean isUserExists, checkUserCallExecuted;
    private DatePickerDialog datePickerDialog;
    private boolean isMedicalAdvice;;
    private CheckBox chb_agree_privacy, cbVaccineGuide, cbCovidConcern, cbManagingBreathlessness, cbManageVoiceIssue,
            cbManageEating, cbDealProblems, cbMentalHealth, cbExercises, cbOthers;
    private TextView txt_privacy;
    private EditText et_medical_advice_extra, et_medical_advice_additional;

    public static void start(Context context, boolean medicalAdvice) {
        Intent starter = new Intent(context, IdentificationActivity.class);
        starter.putExtra(EXTRA_MEDICAL_ADVICE, medicalAdvice);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        String language = sessionManager.getAppLanguage();
        //In case of crash still the app should hold the current lang fix.
        if (!language.equalsIgnoreCase("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        sessionManager.setCurrentLang(getResources().getConfiguration().locale.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        setTitle(R.string.title_activity_identification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cpd = new CustomProgressDialog(this);
        i_privacy = getIntent();
        context = IdentificationActivity.this;
        privacy_value = i_privacy.getStringExtra("privacy"); //privacy_accept value retrieved from previous act.

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // sessionManager = new SessionManager(this);
        mFirstName = findViewById(R.id.identification_first_name);
        mFirstName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Name}); //maxlength 25

        mMiddleName = findViewById(R.id.identification_middle_name);
        mMiddleName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Name}); //maxlength 25

        mLastName = findViewById(R.id.identification_last_name);
        mLastName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Name}); //maxlength 25

        mDOB = findViewById(R.id.identification_birth_date_text_view);
        mPhoneNum = findViewById(R.id.identification_phone_number);

     /*   mPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(mPhoneNum.getText().toString().trim().length() < 10)
                        mPhoneNum.setError("Enter 10 digits");
                    else
                        mPhoneNum.setError(null);
                }
            }
        });*/

        mAge = findViewById(R.id.identification_age);
        mAddress1 = findViewById(R.id.identification_address1);
        mAddress1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50), inputFilter_Name}); //maxlength 50

        mAddress2 = findViewById(R.id.identification_address2);
        mAddress2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50), inputFilter_Name}); //maxlength 50

        mCity = findViewById(R.id.identification_city);
        mCity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25

//        stateText = findViewById(R.id.identification_state);
        autocompleteState = findViewById(R.id.autocomplete_state);
//        mState = findViewById(R.id.spinner_state);
        mPostal = findViewById(R.id.identification_postal_code);
        countryText = findViewById(R.id.identification_country);
        mCountry = findViewById(R.id.spinner_country);
        mGenderM = findViewById(R.id.identification_gender_male);
        mGenderF = findViewById(R.id.identification_gender_female);
        mRelationship = findViewById(R.id.identification_relationship);
        mRelationship.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25

        // mOccupation = findViewById(R.id.identification_occupation);
        // mOccupation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25

        mCaste = findViewById(R.id.spinner_caste);
        mEducation = findViewById(R.id.spinner_education);
        mEconomicStatus = findViewById(R.id.spinner_economic_status);
        casteText = findViewById(R.id.identification_caste);
        educationText = findViewById(R.id.identification_education);
        economicText = findViewById(R.id.identification_econiomic_status);

        casteLayout = findViewById(R.id.identification_txtlcaste);
        economicLayout = findViewById(R.id.identification_txtleconomic);
        educationLayout = findViewById(R.id.identification_txtleducation);
        countryStateLayout = findViewById(R.id.identification_llcountry_state);
        //  mImageView = findViewById(R.id.imageview_id_picture);

        //Spinner
       /* occupation_spinner = findViewById(R.id.occupation_spinner);
        occupation_edittext = findViewById(R.id.occupation_edittext);
        bankaccount_spinner = findViewById(R.id.bankaccount_spinner);
        mobilephone_spinner = findViewById(R.id.mobilephone_spinner);
        whatsapp_spinner = findViewById(R.id.whatsapp_spinner);
        source_of_water_spinner = findViewById(R.id.source_of_water_spinner);
        howtomake_water_safe_spinner = findViewById(R.id.howtomake_water_safe_spinner);
        watersafe_edittext = findViewById(R.id.watersafe_edittext);
        water_availability_spinner = findViewById(R.id.water_availability_spinner);
        toilet_facility_spinner = findViewById(R.id.toilet_facility_spinner);
        toiletfacility_edittext = findViewById(R.id.toiletfacility_edittext);
        structure_of_house_spinner = findViewById(R.id.structure_of_house_spinner);*/

        //HOH - Checkbox
       /* familyhead_checkbox = findViewById(R.id.familyhead_checkbox);
        time_water_checkbox = findViewById(R.id.time_water_checkbox);
        hectars_land_checkbox = findViewById(R.id.hectars_land_checkbox);*/

        //EditText
       /* time_water_editText = findViewById(R.id.time_water_editText);
        hectars_land_editText = findViewById(R.id.hectars_land_editText);
        no_of_member_edittext = findViewById(R.id.no_of_member_edittext);
        no_of_staying_members_edittext = findViewById(R.id.no_of_staying_members_edittext);*/

        //Cardview
        // cardview_household = findViewById(R.id.cardview_household);

//Initialize the local database to store patient information

        Intent intent = this.getIntent(); // The intent was passed to the activity
        if (intent != null) {
            if (intent.hasExtra("patientUuid")) {
                this.setTitle(R.string.update_patient_identification);
                patientID_edit = intent.getStringExtra("patientUuid");
                patient1.setUuid(patientID_edit);
                setscreen(patientID_edit);
                mPhoneNum.setEnabled(false);
            }
            isMedicalAdvice = intent.getBooleanExtra(EXTRA_MEDICAL_ADVICE, false);
        }
//        if (sessionManager.valueContains("licensekey"))
        if (!sessionManager.getLicenseKey().isEmpty())
            hasLicense = true;
        //Check for license key and load the correct config file
        try {
            JSONObject obj = null;
            if (hasLicense) {
                obj = new JSONObject(Objects.requireNonNullElse
                        (FileUtils.readFileRoot(AppConstants.CONFIG_FILE_NAME, context),
                                String.valueOf(FileUtils.encodeJSON(context, AppConstants.CONFIG_FILE_NAME)))); //Load the config file
            } else {
                obj = new JSONObject(String.valueOf(FileUtils.encodeJSON(this, AppConstants.CONFIG_FILE_NAME)));
            }

            //Display the fields on the Add Patient screen as per the config file
            if (obj.getBoolean("mFirstName")) {
                mFirstName.setVisibility(View.VISIBLE);
            } else {
                mFirstName.setVisibility(View.GONE);
            }

            if (obj.getBoolean("mMiddleName")) {
                mMiddleName.setVisibility(View.VISIBLE);
            } else {
                mMiddleName.setVisibility(View.GONE);
            }

            if (obj.getBoolean("mLastName")) {
                mLastName.setVisibility(View.VISIBLE);
            } else {
                mLastName.setVisibility(View.GONE);
            }

            if (obj.getBoolean("mDOB")) {
                mDOB.setVisibility(View.VISIBLE);
            } else {
                mDOB.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mPhoneNum")) {
                mPhoneNum.setVisibility(View.VISIBLE);
            } else {
                mPhoneNum.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mAge")) {
                mAge.setVisibility(View.VISIBLE);
            } else {
                mAge.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mAddress1")) {
                mAddress1.setVisibility(View.VISIBLE);
            } else {
                mAddress1.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mAddress2")) {
                mAddress2.setVisibility(View.VISIBLE);
            } else {
                mAddress2.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mCity")) {
                mCity.setVisibility(View.VISIBLE);
            } else {
                mCity.setVisibility(View.GONE);
            }

            if (obj.getBoolean("countryStateLayout")) {
                countryStateLayout.setVisibility(View.VISIBLE);
            } else {
                countryStateLayout.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mPostal")) {
                mPostal.setVisibility(View.VISIBLE);
            } else {
                mPostal.setVisibility(View.GONE);
            }

            if (obj.getBoolean("mGenderM")) {
                mGenderM.setVisibility(View.VISIBLE);
            } else {
                mGenderM.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mGenderF")) {
                mGenderF.setVisibility(View.VISIBLE);
            } else {
                mGenderF.setVisibility(View.GONE);
            }
            if (obj.getBoolean("mRelationship")) {
                mRelationship.setVisibility(View.VISIBLE);
            } else {
                mRelationship.setVisibility(View.GONE);
            }
//            if (obj.getBoolean("mOccupation")) {
//                mOccupation.setVisibility(View.VISIBLE);
//            } else {
//                mOccupation.setVisibility(View.GONE);
//            }
            if (obj.getBoolean("casteLayout")) {
                casteLayout.setVisibility(View.VISIBLE);
            } else {
                casteLayout.setVisibility(View.GONE);
            }
            if (obj.getBoolean("educationLayout")) {
                educationLayout.setVisibility(View.VISIBLE);
            } else {
                educationLayout.setVisibility(View.GONE);
            }
            if (obj.getBoolean("economicLayout")) {
                economicLayout.setVisibility(View.VISIBLE);
            } else {
                economicLayout.setVisibility(View.GONE);
            }
            country1 = obj.getString("mCountry");
            state = obj.getString("mState");

            if (country1.equalsIgnoreCase("India")) {
                EditTextUtils.setEditTextMaxLength(10, mPhoneNum);
            } else if (country1.equalsIgnoreCase("Philippines")) {
                EditTextUtils.setEditTextMaxLength(11, mPhoneNum);
            }

        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
//            Issue #627
//            added the catch exception to check the config and throwing back to setup activity
            Toast.makeText(getApplicationContext(), "JsonException" + e, Toast.LENGTH_LONG).show();
            showAlertDialogButtonClicked(e.toString());
        }

        //setting the fields when user clicks edit details
        mFirstName.setText(patient1.getFirst_name());
        mMiddleName.setText(patient1.getMiddle_name());
        mLastName.setText(patient1.getLast_name());

  /*      if(patient1.getDate_of_birth() != null) {
            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                mDOB.setText(StringUtils.en__hi_dob(patient1.getDate_of_birth()));
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                mDOB.setText(StringUtils.en__or_dob(patient1.getDate_of_birth()));
            } else {
                mDOB.setText(patient1.getDate_of_birth());
            }
        }
        else {
            mDOB.setText(patient1.getDate_of_birth());
        }*/
        mDOB.setText(patient1.getDate_of_birth());


        mPhoneNum.setText(patient1.getPhone_number());
        mAddress1.setText(patient1.getAddress1());
        mAddress2.setText(patient1.getAddress2());
        mCity.setText(patient1.getCity_village());
        mPostal.setText(patient1.getPostal_code());
        mRelationship.setText(patient1.getSdw());
        autocompleteState.setText(patient1.getState_province());

       /* if (patient1.getPatient_photo() != null && !patient1.getPatient_photo().trim().isEmpty())
            mImageView.setImageBitmap(BitmapFactory.decodeFile(patient1.getPatient_photo()));
*/
        Resources res = getResources();
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, R.layout.custom_spinner);
        //countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountry.setAdapter(countryAdapter);

//        ArrayAdapter<CharSequence> casteAdapter = ArrayAdapter.createFromResource(this,
//                R.array.caste, R.layout.custom_spinner);
//        //countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mCaste.setAdapter(casteAdapter);

        try { //Caste adapter setting...
            String casteLanguage = "caste_" + sessionManager.getAppLanguage();
            int castes = res.getIdentifier(casteLanguage, "array", getApplicationContext().getPackageName());
            if (castes != 0) {
                casteAdapter = ArrayAdapter.createFromResource(this,
                        castes, R.layout.custom_spinner);

            }
            mCaste.setAdapter(casteAdapter);
        } catch (Exception e) {
            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
        try { //Economic adapter setting...
            String economicLanguage = "economic_" + sessionManager.getAppLanguage();
            int economics = res.getIdentifier(economicLanguage, "array", getApplicationContext().getPackageName());
            if (economics != 0) {
                economicStatusAdapter = ArrayAdapter.createFromResource(this,
                        economics, R.layout.custom_spinner);
            }
            // countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEconomicStatus.setAdapter(economicStatusAdapter);
        } catch (Exception e) {
            Toast.makeText(this, R.string.economic_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
        try { //Education adapter setting....
            String educationLanguage = "education_" + sessionManager.getAppLanguage();
            int educations = res.getIdentifier(educationLanguage, "array", getApplicationContext().getPackageName());
            if (educations != 0) {
                educationAdapter = ArrayAdapter.createFromResource(this,
                        educations, R.layout.custom_spinner);

            }
            // countryAdapter.setDropDownViewResource(R.layout.custom_spinner);
            mEducation.setAdapter(educationAdapter);
        } catch (Exception e) {
            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }

        //Occupation Adapter ...
      /*  try {
            String occupationLanguage = "occupation_spinner_" + sessionManager.getAppLanguage();
            int occupation_id = res.getIdentifier(occupationLanguage, "array", getApplicationContext().getPackageName());
            if (occupation_id != 0) {
                occupation_adapt = ArrayAdapter.createFromResource(this,
                        occupation_id, android.R.layout.simple_spinner_dropdown_item);

            }

            occupation_spinner.setAdapter(occupation_adapt);
        } catch (Exception e) {
            Toast.makeText(this, "Occupation values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }*/

        //Household Head
//        occupation_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.occupation_spinner));

/*
        occupation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equalsIgnoreCase("[Describe]") ||
                        parent.getSelectedItem().toString().equalsIgnoreCase("वर्णन करे")) {
                    occupation_edittext.setVisibility(View.VISIBLE);
                    occupation_edittext.requestFocus();
                    occupation_edittext.setFocusable(true);
                    occupation_edittext.setFocusableInTouchMode(true);
                } else {
                    occupation_edittext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        //Bank Account Adapter ...
/*
        try {
            String bankaccountLanguage = "bank_account_spinner_" + sessionManager.getAppLanguage();
            int bankaccount_id = res.getIdentifier(bankaccountLanguage, "array", getApplicationContext().getPackageName());
            if (bankaccount_id != 0) {
                bankaccount_adapt = ArrayAdapter.createFromResource(this,
                        bankaccount_id, android.R.layout.simple_spinner_dropdown_item);
            }
            bankaccount_spinner.setAdapter(bankaccount_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "BankAccount values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

//        bankaccount_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.bank_account_spinner));

        //Mobile Type Adapter ...
/*
        try {
            String mobileTypeLanguage = "mobilephone_spinner_" + sessionManager.getAppLanguage();
            int mobiletype_id = res.getIdentifier(mobileTypeLanguage, "array", getApplicationContext().getPackageName());
            if (mobiletype_id != 0) {
                mobile_adapt = ArrayAdapter.createFromResource(this,
                        mobiletype_id, android.R.layout.simple_spinner_dropdown_item);
            }
            mobilephone_spinner.setAdapter(mobile_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Mobile Type values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

//        mobile_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.mobilephone_spinner));

//        whatsapp_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.familymember_whatsapp));

        //Whatsapp App Adapter ...
/*
        try {
            String whatsappLanguage = "familymember_whatsapp_" + sessionManager.getAppLanguage();
            int whatsapp_id = res.getIdentifier(whatsappLanguage, "array", getApplicationContext().getPackageName());
            if (whatsapp_id != 0) {
                whatsapp_adapt = ArrayAdapter.createFromResource(this,
                        whatsapp_id, android.R.layout.simple_spinner_dropdown_item);
            }
            whatsapp_spinner.setAdapter(whatsapp_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Whatsapp values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

//        sourcewater_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.sourcewater_spinner));

        //Source Water Adapter ...
/*
        try {
            String sourcewaterLanguage = "sourcewater_spinner_" + sessionManager.getAppLanguage();
            int sourcewater_id = res.getIdentifier(sourcewaterLanguage, "array", getApplicationContext().getPackageName());
            if (sourcewater_id != 0) {
                sourcewater_adapt = ArrayAdapter.createFromResource(this,
                        sourcewater_id, android.R.layout.simple_spinner_dropdown_item);
            }
            source_of_water_spinner.setAdapter(sourcewater_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Water Source values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

//        watersafe_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.howtomake_water_safe));

        // Water Safe Adapter ...
/*
        try {
            String watersafeLanguage = "howtomake_water_safe_" + sessionManager.getAppLanguage();
            int watersafe_id = res.getIdentifier(watersafeLanguage, "array", getApplicationContext().getPackageName());
            if (watersafe_id != 0) {
                watersafe_adapt = ArrayAdapter.createFromResource(this,
                        watersafe_id, android.R.layout.simple_spinner_dropdown_item);
            }
            howtomake_water_safe_spinner.setAdapter(watersafe_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Water Safe values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

/*
        howtomake_water_safe_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equalsIgnoreCase("Other[Enter]") ||
                        parent.getSelectedItem().toString().equalsIgnoreCase("अन्य [दर्ज करें]")) {
                    watersafe_edittext.setVisibility(View.VISIBLE);
                    watersafe_edittext.requestFocus();
                    watersafe_edittext.setFocusable(true);
                    watersafe_edittext.setFocusableInTouchMode(true);
                } else {
                    watersafe_edittext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

//        availa_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.water_availability_spinner));

        // Water Availability Adapter ...
/*
        try {
            String wateravailLanguage = "water_availability_spinner_" + sessionManager.getAppLanguage();
            int wateravail_id = res.getIdentifier(wateravailLanguage, "array", getApplicationContext().getPackageName());
            if (wateravail_id != 0) {
                availa_adapt = ArrayAdapter.createFromResource(this,
                        wateravail_id, android.R.layout.simple_spinner_dropdown_item);
            }
            water_availability_spinner.setAdapter(availa_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Water Availability values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

//        toiletfacility_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.toiletFacility_spinner));

        // Toilet Facility Adapter ...
/*
        try {
            String toiletFacilityLanguage = "toiletFacility_spinner_" + sessionManager.getAppLanguage();
            int toiletfacil_id = res.getIdentifier(toiletFacilityLanguage, "array", getApplicationContext().getPackageName());
            if (toiletfacil_id != 0) {
                toiletfacility_adapt = ArrayAdapter.createFromResource(this,
                        toiletfacil_id, android.R.layout.simple_spinner_dropdown_item);
            }
            toilet_facility_spinner.setAdapter(toiletfacility_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "Toilet Facility values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/


/*
        toilet_facility_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equalsIgnoreCase("Other [Enter]") ||
                        parent.getSelectedItem().toString().equalsIgnoreCase("अन्य [दर्ज करें]")) {
                    toiletfacility_edittext.setVisibility(View.VISIBLE);
                    toiletfacility_edittext.requestFocus();
                    toiletfacility_edittext.setFocusable(true);
                    toiletfacility_edittext.setFocusableInTouchMode(true);
                } else {
                    toiletfacility_edittext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

//        structure_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
//                getResources().getStringArray(R.array.structure_house));

        // House Structure Adapter ...
/*
        try {
            String houseStructureLanguage = "structure_house_" + sessionManager.getAppLanguage();
            int houseStruct_id = res.getIdentifier(houseStructureLanguage, "array", getApplicationContext().getPackageName());
            if (houseStruct_id != 0) {
                structure_adapt = ArrayAdapter.createFromResource(this,
                        houseStruct_id, android.R.layout.simple_spinner_dropdown_item);
            }
            structure_of_house_spinner.setAdapter(structure_adapt);

        } catch (Exception e) {
            Toast.makeText(this, "House Structure values are missing", Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
*/

        //editText values values are set for the household fields ...
     /*   no_of_member_edittext.setText(patient1.getNo_of_family_members());
        no_of_staying_members_edittext.setText(patient1.getNo_of_family_currently_live());*/

/*
        if (patient1.getTime_travel_water() != null || patient1.getHectars_land() != null) {
            if (patient1.getTime_travel_water().equalsIgnoreCase("Declined to answer")) {
                time_water_editText.setVisibility(View.GONE);
                time_water_checkbox.setChecked(true);
            } else {
                time_water_editText.setVisibility(View.VISIBLE);
                time_water_editText.setText(patient1.getTime_travel_water());
            }
            if (patient1.getHectars_land().equalsIgnoreCase("Declined to answer")) {
                hectars_land_editText.setVisibility(View.GONE);
                hectars_land_checkbox.setChecked(true);
            } else {
                hectars_land_editText.setVisibility(View.VISIBLE);
                hectars_land_editText.setText(patient1.getHectars_land());
            }
        }
*/


        if (null == patientID_edit || patientID_edit.isEmpty()) {
            generateUuid();

        }

/*
        time_water_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTimePicker = new MaterialAlertDialogBuilder(IdentificationActivity.this, R.style.AlertDialogStyle);
                mTimePicker.setTitle(R.string.identification_screen_prompt_time);
                final LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.dialog_2_numbers_picker, null);
                mTimePicker.setView(convertView);
                NumberPicker hourPicker = convertView.findViewById(R.id.dialog_2_numbers_quantity);
                NumberPicker minsPicker = convertView.findViewById(R.id.dialog_2_numbers_unit);


                final TextView middleText = convertView.findViewById(R.id.dialog_2_numbers_text);
                final TextView endText = convertView.findViewById(R.id.dialog_2_numbers_text_3);

                int totalDays = today.getActualMaximum(Calendar.DAY_OF_MONTH);

                middleText.setText(getString(R.string.identification_screen_picker_hours));
                endText.setText(getString(R.string.identification_screen_picker_minute));


                hourPicker.setMinValue(0);
                hourPicker.setMaxValue(24);
                minsPicker.setMinValue(0);
                minsPicker.setMaxValue(60);

                EditText hourText = hourPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
                EditText minsText = minsPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));

                hourPicker.setValue(mTimeHours);
                minsPicker.setValue(mTimeMins);

                //year
                EditTextUtils.returnEditextValues(new IReturnValues() {
                    @Override
                    public void onReturnValue(String value) {
                        mTimeHours = Integer.valueOf(value);
                    }
                }, hourText);

                //month
                EditTextUtils.returnEditextValues(new IReturnValues() {
                    @Override
                    public void onReturnValue(String value) {
                        mTimeMins = Integer.valueOf(value);
                    }
                }, minsText);


                mTimePicker.setPositiveButton(R.string.generic_ok, (dialog, which) -> {
                    String ageString = mTimeHours + " " + getString(R.string.identification_screen_picker_hours) + " - " +
                            mTimeMins + " " + getString(R.string.identification_screen_picker_minute);
                    time_water_editText.setText(ageString);

                    dialog.dismiss();
                });
                mTimePicker.setNegativeButton(R.string.generic_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = mTimePicker.show();
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);

            }
        });
*/

/*
        time_water_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    time_water_checkbox.setError(null);
                    time_water_editText.setError(null);
                    time_water_editText.setVisibility(View.GONE);
                } else {
                    time_water_editText.setVisibility(View.VISIBLE);
                }
            }
        });
*/

/*
        hectars_land_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hectars_land_checkbox.setError(null);
                    hectars_land_editText.setError(null);
                    hectars_land_editText.setVisibility(View.GONE);
                } else {
                    hectars_land_editText.setVisibility(View.VISIBLE);
                }
            }
        });
*/

        // setting radio button automatically according to the databse when user clicks edit details
        if (patientID_edit != null) {

            if (patient1.getGender().equals("M")) {
                mGenderM.setChecked(true);
                if (mGenderF.isChecked())
                    mGenderF.setChecked(false);
                Log.v(TAG, "yes");
            } else {
                mGenderF.setChecked(true);
                if (mGenderM.isChecked())
                    mGenderM.setChecked(false);
                Log.v(TAG, "yes");
            }

        }
        if (mGenderM.isChecked()) {
            mGender = "M";
        } else {
            mGender = "F";
        }
        if (patientID_edit != null) {
            // setting country according database
            mCountry.setSelection(countryAdapter.getPosition(String.valueOf(patient1.getCountry())));

           /* if (patient1.getEducation_level().equals(getResources().getString(R.string.not_provided)))
                mEducation.setSelection(0);
            else {
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String education = switch_hi_education_edit(patient1.getEducation_level());
                    mEducation.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    mEducation.setSelection(educationAdapter != null ? educationAdapter.getPosition(patient1.getEducation_level()) : 0);
                } else {
                    mEducation.setSelection(educationAdapter != null ? educationAdapter.getPosition(patient1.getEducation_level()) : 0);
                }
            }
            //mEducation.setSelection(educationAdapter != null ? educationAdapter.getPosition(patient1.getEducation_level()) : 0);
            if (educationAdapter == null) {
                Toast.makeText(context, "Education Level: " + patient1.getEducation_level(), Toast.LENGTH_LONG).show();
            }*/


          /*  if (patient1.getEconomic_status().equals(getResources().getString(R.string.not_provided)))
                mEconomicStatus.setSelection(0);
            else {
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String economic = switch_hi_economic_edit(patient1.getEconomic_status());
                    mEconomicStatus.setSelection(economicStatusAdapter.getPosition(economic));
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    mEconomicStatus.setSelection(economicStatusAdapter.getPosition(patient1.getEconomic_status()));
                } else {
                    mEconomicStatus.setSelection(economicStatusAdapter.getPosition(patient1.getEconomic_status()));
                }
            }*/
            // mEconomicStatus.setSelection(economicStatusAdapter.getPosition(patient1.getEconomic_status()));

        /*    if (patient1.getCaste().equals(getResources().getString(R.string.not_provided)))
                mCaste.setSelection(0);
            else {
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String caste = switch_hi_caste_edit(patient1.getCaste());
                    mCaste.setSelection(casteAdapter.getPosition(caste));
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    mCaste.setSelection(casteAdapter.getPosition(patient1.getCaste()));
                } else {
                    mCaste.setSelection(casteAdapter.getPosition(patient1.getCaste()));
                }

            }*/

            //Houselhold Head...
          /*  if (patient1.getNo_of_family_members() != null && !patient1.getNo_of_family_members().equalsIgnoreCase("")
                    && !patient1.getNo_of_family_members().isEmpty()) {
                familyhead_checkbox.setChecked(true);
                cardview_household.setVisibility(View.VISIBLE);
                //sessionManager.setHOH_checkbox(false);
            } else {
                familyhead_checkbox.setChecked(false);
                cardview_household.setVisibility(View.GONE);
            }*/

/*
            if (patient1.getOccupation() != null && !patient1.getOccupation().equalsIgnoreCase("")) {
                String occupation_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    occupation_Transl = StringUtils.switch_hi_occupation_edit(patient1.getOccupation());
                } else {
                    occupation_Transl = patient1.getOccupation();
                }

                int spinner_position = occupation_adapt.getPosition(occupation_Transl);
                if (spinner_position >= 0) {
                    occupation_spinner.setSelection(spinner_position); //user selected value items from spinner
                }
                //since here we will show the value of the dynamic occupation value...
                else {
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        occupation_spinner.setSelection(occupation_adapt.getPosition("वर्णन करे"));
                    } else {
                        occupation_spinner.setSelection(occupation_adapt.getPosition("[Describe]"));
                    }

                    occupation_edittext.setVisibility(View.VISIBLE);
                    occupation_edittext.setText(patient1.getOccupation());
                }

            }
*/
/*
            if (patient1.getBank_account() != null && !patient1.getBank_account().equalsIgnoreCase("")) {
                String bankacc_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    bankacc_Transl = StringUtils.switch_hi_bankaccount_edit(patient1.getBank_account());
                } else {
                    bankacc_Transl = patient1.getBank_account();
                }

                int spinner_position = bankaccount_adapt.getPosition(bankacc_Transl);
                bankaccount_spinner.setSelection(spinner_position);
            }
*/
/*
            if (patient1.getMobile_type() != null && !patient1.getMobile_type().equalsIgnoreCase("")) {
                String mobile_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    mobile_Transl = StringUtils.switch_hi_mobiletype_edit(patient1.getMobile_type());
                } else {
                    mobile_Transl = patient1.getMobile_type();
                }

                int spinner_position = mobile_adapt.getPosition(mobile_Transl);
                mobilephone_spinner.setSelection(spinner_position);
            }
*/
/*
            if (patient1.getWhatsapp_mobile() != null && !patient1.getWhatsapp_mobile()
                    .equalsIgnoreCase("")) {
                String whatsapp_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    whatsapp_Transl = StringUtils.switch_hi_whatsapp_edit(patient1.getWhatsapp_mobile());
                } else {
                    whatsapp_Transl = patient1.getWhatsapp_mobile();
                }

                int spinner_position = whatsapp_adapt.getPosition(whatsapp_Transl);
                whatsapp_spinner.setSelection(spinner_position);
            }
*/
/*
            if (patient1.getSource_of_water() != null && !patient1.getSource_of_water()
                    .equalsIgnoreCase("")) {

                String watersource_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    watersource_Transl = StringUtils.switch_hi_watersource_edit(patient1.getSource_of_water());
                } else {
                    watersource_Transl = patient1.getSource_of_water();
                }

                int spinner_position = sourcewater_adapt.getPosition(watersource_Transl);
                source_of_water_spinner.setSelection(spinner_position);
            }
*/
/*
            if (patient1.getWater_safe() != null && !patient1.getWater_safe().equalsIgnoreCase("")) {

                String watersafe_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    watersafe_Transl = StringUtils.switch_hi_watersafe_edit(patient1.getWater_safe());
                } else {
                    watersafe_Transl = patient1.getWater_safe();
                }

                int spinner_position = watersafe_adapt.getPosition(watersafe_Transl);
                howtomake_water_safe_spinner.setSelection(spinner_position);

                if (spinner_position >= 0) {
                    howtomake_water_safe_spinner.setSelection(spinner_position); //user selected value items from spinner
                }
                //sicne we will have to show our dynamuic values here..
                else {
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        howtomake_water_safe_spinner.setSelection(watersafe_adapt.getPosition("अन्य [दर्ज करें]"));
                    } else {
                        howtomake_water_safe_spinner.setSelection(watersafe_adapt.getPosition("Other[Enter]"));
                    }

                    watersafe_edittext.setVisibility(View.VISIBLE);
                    watersafe_edittext.setText(patient1.getWater_safe());
                }
            }
*/

/*
            if (patient1.getWater_availability() != null && !patient1.getWater_availability()
                    .equalsIgnoreCase("")) {

                String wateravail_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    wateravail_Transl = StringUtils.switch_hi_wateravail_edit(patient1.getWater_availability());
                } else {
                    wateravail_Transl = patient1.getWater_availability();
                }

                int spinner_position = availa_adapt.getPosition(wateravail_Transl);
                water_availability_spinner.setSelection(spinner_position);
            }
*/
/*
            if (patient1.getToilet_facility() != null && !patient1.getToilet_facility()
                    .equalsIgnoreCase("")) {

                String toiletfacility_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    toiletfacility_Transl = StringUtils.switch_hi_toiletfacil_edit(patient1.getToilet_facility());
                } else {
                    toiletfacility_Transl = patient1.getToilet_facility();
                }

                int spinner_position = toiletfacility_adapt.getPosition(toiletfacility_Transl);
                toilet_facility_spinner.setSelection(spinner_position);

                if (spinner_position >= 0) {
                    toilet_facility_spinner.setSelection(spinner_position); //user selected value items from spinner
                }
                //since we will have to show our dynamic values here..
                else {
                    //on edit the spinner value will be selected based on the current app lang...
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        toilet_facility_spinner.setSelection(toiletfacility_adapt.getPosition("अन्य [दर्ज करें]"));
                    } else {
                        toilet_facility_spinner.setSelection(toiletfacility_adapt.getPosition("Other[Enter]"));
                    }

                    toiletfacility_edittext.setVisibility(View.VISIBLE);
                    toiletfacility_edittext.setText(patient1.getToilet_facility());
                }
            }
*/

/*
            if (patient1.getStructure_house() != null && !patient1.getStructure_house()
                    .equalsIgnoreCase("")) {

                String housestruct_Transl = "";
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    housestruct_Transl = StringUtils.switch_hi_housestructure_edit(patient1.getStructure_house());
                } else {
                    housestruct_Transl = patient1.getStructure_house();
                }

                int spinner_position = structure_adapt.getPosition(housestruct_Transl);
                structure_of_house_spinner.setSelection(spinner_position);
            }
*/

            //mCaste.setSelection(casteAdapter.getPosition(patient1.getCaste())); //edit...
        } else {
            mCountry.setSelection(countryAdapter.getPosition(country1));
        }


//        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.state_error, R.layout.custom_spinner);
//        //  stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mState.setAdapter(stateAdapter);

        // Get the string array
        String[] countries = getResources().getStringArray(R.array.states_india);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        autocompleteState.setAdapter(adapter);


        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    String country = adapterView.getItemAtPosition(i).toString();

                    if (country.matches("India")) {
                        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                R.array.states_india, R.layout.custom_spinner);
                        // stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mState.setAdapter(stateAdapter);
                        // setting state according database when user clicks edit details

//                        if (patientID_edit != null)
//                            mState.setSelection(stateAdapter.getPosition(String.valueOf(patient1.getState_province())));
//                        else
//                            mState.setSelection(stateAdapter.getPosition(state));

                    } else if (country.matches("United States")) {
                        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                R.array.states_us, R.layout.custom_spinner);
                        // stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mState.setAdapter(stateAdapter);

//                        if (patientID_edit != null) {
//
//                            mState.setSelection(stateAdapter.getPosition(String.valueOf(patient1.getState_province())));
//                        }
                    } else if (country.matches("Philippines")) {
                        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                R.array.states_philippines, R.layout.custom_spinner);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mState.setAdapter(stateAdapter);
//
//                        if (patientID_edit != null) {
//                            mState.setSelection(stateAdapter.getPosition(String.valueOf(patient1.getState_province())));
//                        } else {
//                            mState.setSelection(stateAdapter.getPosition("Bukidnon"));
//                        }
                    }
                } else {
                    ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                            R.array.state_error, R.layout.custom_spinner);
                    // stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mState.setAdapter(stateAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!mAge.getText().toString().isEmpty() || !mAge.getText().toString().equals("")) {
                    int dob_int = Integer.parseInt(mAge.getText().toString());
                    if (dob_int < 1) {
                        mAge.getText().clear();
                        mAge.setError(getString(R.string.age_lessthan_1));
                        mAge.setFocusable(true);
                        mAge.setFocusableInTouchMode(true);
                        mAge.requestFocus();
                        return;
                    } else if (dob_int > 120) {
                        mAge.getText().clear();
                        mAge.setError(getString(R.string.age_greaterthan_120));
                        mAge.setFocusable(true);
                        mAge.setFocusableInTouchMode(true);
                        mAge.requestFocus();
                        return;
                    }
                    else {
                        //do nothing...
                    }
                }
            }
        });

//        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String state = parent.getItemAtPosition(position).toString();
//                if (state.matches("Odisha")) {
//                    //Creating the instance of ArrayAdapter containing list of fruit names
//                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
//                            R.array.odisha_villages, R.layout.custom_spinner);
//                    mCity.setThreshold(1);//will start working from first character
//                    mCity.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//                } else if (state.matches("Bukidnon")) {
//                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
//                            R.array.bukidnon_villages, R.layout.custom_spinner);
//                    mCity.setThreshold(1);//will start working from first character
//                    mCity.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//                } else {
//                    mCity.setAdapter(null);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        mGenderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        mGenderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
/*
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientTemp = "";
                if (patientUuid.equalsIgnoreCase("")) {
                    patientTemp = patientID_edit;
                } else {
                    patientTemp = patientUuid;
                }
                File filePath = new File(AppConstants.IMAGE_PATH + patientTemp);
                if (!filePath.exists()) {
                    filePath.mkdir();
                }
                Intent cameraIntent = new Intent(IdentificationActivity.this, CameraActivity.class);

                // cameraIntent.putExtra(CameraActivity.SHOW_DIALOG_MESSAGE, getString(R.string.camera_dialog_default));
                cameraIntent.putExtra(CameraActivity.SET_IMAGE_NAME, patientTemp);
                cameraIntent.putExtra(CameraActivity.SET_IMAGE_PATH, filePath.toString());
                startActivityForResult(cameraIntent, CameraActivity.TAKE_IMAGE);
            }
        });
*/
      /*  mDOBYear = today.get(Calendar.YEAR);
        mDOBMonth = today.get(Calendar.MONTH);
        mDOBDay = today.get(Calendar.DAY_OF_MONTH);*/
        //DOB is set using an AlertDialog
        //  Locale.setDefault(Locale.ENGLISH);

/*
        mDOBPicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Set the DOB calendar to the date selected by the user
                dob.set(year, monthOfYear, dayOfMonth);
                mDOB.setError(null);
                mAge.setError(null);
                //Set Maximum date to current date because even after bday is less than current date
                // it goes to check date is set after today...
                mDOBPicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                //   Locale.setDefault(Locale.ENGLISH);
                //Formatted so that it can be read the way the user sets
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",
                        Locale.forLanguageTag(sessionManager.getAppLanguage()));

                dob.set(year, monthOfYear, dayOfMonth);
                dob_indexValue = monthOfYear; //fetching the inex value of month selected...
                String dobString = simpleDateFormat.format(dob.getTime());
                mDOB.setText(dobString);
                mDOBYear = year;
                mDOBMonth = monthOfYear;
                mDOBDay = dayOfMonth;
//                mDOB.setText(mDOBDay + getResources().getString(R.));


                String age = getYear(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DATE),
                        today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
                //get years months days
                String[] frtData = age.split("-");

                String[] yearData = frtData[0].split(" ");
                String[] monthData = frtData[1].split(" ");
                String[] daysData = frtData[2].split(" ");

                mAgeYears = Integer.valueOf(yearData[0]);
                mAgeMonths = Integer.valueOf(monthData[1]);
                mAgeDays = Integer.valueOf(daysData[1]);
                String ageString = mAgeYears + getString(R.string.identification_screen_text_years) + " - " +
                        mAgeMonths + getString(R.string.identification_screen_text_months) + " - " +
                        mAgeDays + getString(R.string.days);
                mAge.setText(ageString);

//                mAge.setText(age);

            }
        }, mDOBYear, mDOBMonth, mDOBDay);
*/

        //DOB Picker is shown when clicked
       /* mDOBPicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDOBPicker.show();
            }
        });*/

        //if patient update then age will be set
        if (patientID_edit != null) {
            //dob to be displayed based on translation...
           /* String dob = DateAndTimeUtils.getFormatedDateOfBirthAsView(patient1.getDate_of_birth());
            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                String dob_text = en__hi_dob(dob); //to show text of English into Hindi...
                mDOB.setText(dob_text);
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                String dob_text = en__or_dob(dob); //to show text of English into Odiya...
                mDOB.setText(dob_text);
            }*/
          /*  else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                String dob_text = en__as_dob(dob); //to show text of English into Odiya...
                mDOB.setText(dob_text);
            }*/
           /* else {
                mDOB.setText(dob);
            }*/

            //  mDOB.setText(DateAndTimeUtils.getFormatedDateOfBirthAsView(patient1.getDate_of_birth()));
            //get year month days
            String yrMoDays = DateAndTimeUtils.getAgeInYearMonth(patient1.getDate_of_birth(), context);

            String[] ymdData = DateAndTimeUtils.getAgeInYearMonth(patient1.getDate_of_birth()).split(" ");
            mAgeYears = Integer.valueOf(ymdData[0]);
            mAgeMonths = Integer.valueOf(ymdData[1]);
            mAgeDays = Integer.valueOf(ymdData[2]);
            mAge.setText(yrMoDays);
        }
/*
        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAgePicker = new MaterialAlertDialogBuilder(IdentificationActivity.this, R.style.AlertDialogStyle);
                mAgePicker.setTitle(R.string.identification_screen_prompt_age);
                final LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.dialog_2_numbers_picker, null);
                mAgePicker.setView(convertView);
                NumberPicker yearPicker = convertView.findViewById(R.id.dialog_2_numbers_quantity);
                NumberPicker monthPicker = convertView.findViewById(R.id.dialog_2_numbers_unit);
                NumberPicker dayPicker = convertView.findViewById(R.id.dialog_3_numbers_unit);
                dayPicker.setVisibility(View.VISIBLE);

                final TextView middleText = convertView.findViewById(R.id.dialog_2_numbers_text);
                final TextView endText = convertView.findViewById(R.id.dialog_2_numbers_text_2);
                final TextView dayTv = convertView.findViewById(R.id.dialog_2_numbers_text_3);
                dayPicker.setVisibility(View.VISIBLE);

                int totalDays = today.getActualMaximum(Calendar.DAY_OF_MONTH);
                dayTv.setText(getString(R.string.days));
                middleText.setText(getString(R.string.identification_screen_picker_years));
                endText.setText(getString(R.string.identification_screen_picker_months));


                yearPicker.setMinValue(0);
                yearPicker.setMaxValue(100);
                monthPicker.setMinValue(0);
                monthPicker.setMaxValue(12);

                dayPicker.setMinValue(0);
                dayPicker.setMaxValue(31);

                EditText yearText = yearPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
                EditText monthText = monthPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
                EditText dayText = dayPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));


                yearPicker.setValue(mAgeYears);
                monthPicker.setValue(mAgeMonths);
                dayPicker.setValue(mAgeDays);

                //year
                EditTextUtils.returnEditextValues(new IReturnValues() {
                    @Override
                    public void onReturnValue(String value) {
                        mAgeYears = Integer.valueOf(value);
                    }
                }, yearText);

                //month
                EditTextUtils.returnEditextValues(new IReturnValues() {
                    @Override
                    public void onReturnValue(String value) {
                        mAgeMonths = Integer.valueOf(value);
                    }
                }, monthText);

                //day
                EditTextUtils.returnEditextValues(new IReturnValues() {
                    @Override
                    public void onReturnValue(String value) {
                        mAgeDays = Integer.valueOf(value);
                    }
                }, dayText);
                mAgePicker.setPositiveButton(R.string.generic_ok, (dialog, which) -> {
                    String ageString = mAgeYears + getString(R.string.identification_screen_text_years) + " - " +
                            mAgeMonths + getString(R.string.identification_screen_text_months) + " - " +
                            mAgeDays + getString(R.string.days);
                    mAge.setText(ageString);


                    Calendar calendar = Calendar.getInstance();
                    int curYear = calendar.get(Calendar.YEAR);
                    //int birthYear = curYear - yearPicker.getValue();
                    int birthYear = curYear - mAgeYears;
                    int curMonth = calendar.get(Calendar.MONTH);
                    //int birthMonth = curMonth - monthPicker.getValue();
                    int birthMonth = curMonth - mAgeMonths;
                    //int birthDay = calendar.get(Calendar.DAY_OF_MONTH) - dayPicker.getValue();
                    int birthDay = calendar.get(Calendar.DAY_OF_MONTH) - mAgeDays;
                    mDOBYear = birthYear;
                    mDOBMonth = birthMonth;

                    if (birthDay < 0) {
                        mDOBDay = birthDay + totalDays - 1;
                        mDOBMonth--;

                    } else {
                        mDOBDay = birthDay;
                    }
                    //    Locale.setDefault(Locale.ENGLISH);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",
                            Locale.forLanguageTag(sessionManager.getAppLanguage()));
                    dob.set(mDOBYear, mDOBMonth, mDOBDay);
                    String dobString = simpleDateFormat.format(dob.getTime());
                    mDOB.setText(dobString);
                    mDOBPicker.updateDate(mDOBYear, mDOBMonth, mDOBDay);
                    dob_indexValue = mDOBPicker.getDatePicker().getMonth(); //if user manually selects Age then...
                    Log.d("dd", "dd: " + dob_indexValue);
                    dialog.dismiss();
                });
                mAgePicker.setNegativeButton(R.string.generic_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = mAgePicker.show();
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);
            }
        });
*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (patientID_edit != null) {
                onPatientUpdateClicked(patient1);
            } else {
                if (NetworkConnection.isOnline(getApplication())) {
                    registerUser();
                } else {
//                    onPatientCreateClicked(UUID.randomUUID().toString());
                    Toast.makeText(context, R.string.please_connect_to_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

/*
        familyhead_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyhead_checkbox.isChecked()) {
                    cardview_household.setVisibility(View.VISIBLE);
                    no_of_member_edittext.requestFocus();
                    no_of_member_edittext.setFocusable(true);
                    no_of_member_edittext.setFocusableInTouchMode(true);
                } else {
                    cardview_household.setVisibility(View.GONE);
                }
            }
        });
*/

        List<String> stateArray = Arrays.asList(getResources().getStringArray(R.array.state_values));
        state_spinner = findViewById(R.id.state_spinner);
        city_spinner = findViewById(R.id.city_spinner);
        block_spinner = findViewById(R.id.block_spinner);
        city_spinner.setEnabled(false);
        block_spinner.setEnabled(false);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                R.array.state_values, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(stateAdapter);
        String stateprovince = patient1.getState_province();
        int stateIndex = stateArray.indexOf(stateprovince);
        if (stateIndex > 0) {
            city_spinner.setEnabled(true);
            block_spinner.setEnabled(true);
            state_spinner.setSelection(stateIndex);

            int district_array[] = {R.array.jh_city_values, R.array.mp_city_values, R.array.sk_city_values, R.array.other_city_values};
            ArrayAdapter<CharSequence> cityAdapter = null;
            if(stateIndex == 1) {
                 cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                        district_array[0], android.R.layout.simple_spinner_item);
            }
            else if(stateIndex == 2) {
                 cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                         district_array[1], android.R.layout.simple_spinner_item);
            }
            else if(stateIndex == 3) {
                 cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                         district_array[2], android.R.layout.simple_spinner_item);
            }
            else if(stateIndex == 4) {
                cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                        district_array[3], android.R.layout.simple_spinner_item);
            }
            else {
                //do nothing...
            }
           /* ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    stateIndex == 1 ? R.array.jh_city_values : R.array.mp_city_values, android.R.layout.simple_spinner_item);*/
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            city_spinner.setAdapter(cityAdapter);

           /* ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    stateIndex == 1 ? R.array.jh_block_values : R.array.mp_block_values, android.R.layout.simple_spinner_item);
            blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockAdapter);*/
        }


        //On Edit fetches district value from db and set district-spinner as selected...
        String city_village = patient1.getCity_village();
        mCity.setText(city_village);
        List<String> cityArray = Arrays.asList(getResources().getStringArray(R.array.jh_city_values));
        int cityIndex = cityArray.indexOf(city_village);
        if (cityIndex > 0) {
            //Jharkhand districit....
            if (city_spinner.getAdapter().getCount() > cityIndex) {
                city_spinner.setSelection(cityIndex);
            }
        } else {
            //MP district...
            cityArray = Arrays.asList(getResources().getStringArray(R.array.mp_city_values));
            cityIndex = cityArray.indexOf(city_village);
            if (cityIndex > 0) {
                ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this, R.array.mp_city_values, android.R.layout.simple_spinner_item);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                city_spinner.setAdapter(cityAdapter);
                if (city_spinner.getAdapter().getCount() > cityIndex) {
                    city_spinner.setSelection(cityIndex);
                }
            }
            else {
                //If Sikkim is selected as State and The city in db stored is not present in
                //Jk and MP then check districits of Sikkim...
                cityArray = Arrays.asList(getResources().getStringArray(R.array.sk_city_values));
                cityIndex = cityArray.indexOf(city_village);
                if (cityIndex > 0) {
                    ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                            R.array.sk_city_values, android.R.layout.simple_spinner_item);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_spinner.setAdapter(cityAdapter);
                    if (city_spinner.getAdapter().getCount() > cityIndex) {
                        city_spinner.setSelection(cityIndex);
                    }
                }
                else {
                    //If Other is selected as State and The city in db stored than Other disitricit spinner will be selected....
                    cityArray = Arrays.asList(getResources().getStringArray(R.array.other_city_values));
                    cityIndex = cityArray.indexOf(city_village);
                    if (cityIndex > 0) {
                        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                R.array.other_city_values, android.R.layout.simple_spinner_item);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(cityAdapter);
                        if (city_spinner.getAdapter().getCount() > cityIndex) {
                            city_spinner.setSelection(cityIndex);
                        }
                    }
                }

            }
        }


       // if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Ranchi")) {
           /* ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.rn_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);*/

           /* if (block != null) {
                int spinnerPosition = blockadp.getPosition(block);
                block_spinner.setSelection(spinnerPosition);
            }*/

        String block = patient1.getEducation_level();

        if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Ranchi")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.rn_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //Ranchi Array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.rn_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("East Singhbhum")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.es_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //East Array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.es_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Bokaro")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.bo_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //Bokaro array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.bo_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Dhanbad")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.dh_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //Dhandabad array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.dh_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Khandwa")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.kh_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //Khandwa array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.kh_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Rajgarh")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.rj_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp); //Rajgrah array

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.rj_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }

        //Sikkim - East District - Block selected....
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("East")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.sk_east_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.sk_east_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }

        //Sikkim - West District - Block selected....
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("West")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.sk_west_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.sk_west_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }

        //Sikkim - North District - Block selected....
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("North")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.sk_north_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.sk_north_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }

        //Sikkim - South District - Block selected....
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("South")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.sk_south_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.sk_south_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }

        //Other State - Other District - Block selected...
        else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Other")) {
            ArrayAdapter<CharSequence> blockadp = ArrayAdapter.createFromResource(IdentificationActivity.this,
                    R.array.other_other_block_values, android.R.layout.simple_spinner_item);
            blockadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            block_spinner.setAdapter(blockadp);

            List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.other_other_block_values));
            int blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }









        //   }

       /* List<String> blockArray = Arrays.asList(getResources().getStringArray(R.array.jh_block_values));
        int blockIndex = blockArray.indexOf(block);
        if (blockIndex > 0) {
            if (block_spinner.getAdapter().getCount() > blockIndex) {
                block_spinner.setSelection(blockIndex);
            }
        } else {
            blockArray = Arrays.asList(getResources().getStringArray(R.array.mp_block_values));
            blockIndex = blockArray.indexOf(block);
            if (blockIndex > 0) {
                ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this, R.array.mp_block_values, android.R.layout.simple_spinner_item);
                blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                block_spinner.setAdapter(blockAdapter);
                if (block_spinner.getAdapter().getCount() > blockIndex) {
                    block_spinner.setSelection(blockIndex);
                }
            }
        }*/

        state_spinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city_spinner.setEnabled(true);
                      //  block_spinner.setEnabled(true);

                        int district_array[] = {R.array.jh_city_values, R.array.mp_city_values,
                                R.array.sk_city_values, R.array.other_city_values};
                        ArrayAdapter<CharSequence> adapter = null;
                        if(position == 1) {
                            adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                    district_array[0], android.R.layout.simple_spinner_item);
                        }
                        else if(position == 2) {
                            adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                    district_array[1], android.R.layout.simple_spinner_item);
                        }
                        else if(position == 3) {
                            adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                    district_array[2], android.R.layout.simple_spinner_item);
                        }
                        else if(position == 4) {
                            adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                    district_array[3], android.R.layout.simple_spinner_item);
                        }
                        else {
                            //do nothing...
                        }


                        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                position == 1 ? R.array.jh_city_values : R.array.mp_city_values, android.R.layout.simple_spinner_item);*/
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(adapter);
                        autocompleteState.setText(stateArray.get(position));

                      /*  ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(IdentificationActivity.this,
                                position == 1 ? R.array.jh_block_values : R.array.mp_block_values, android.R.layout.simple_spinner_item);
                        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        block_spinner.setAdapter(blockAdapter);*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }, 1000);

        city_spinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        block_spinner.setEnabled(true);

                        if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Jharkhand")) {
                            if (position > 0) {
                                block_spinner.setEnabled(true);

                                int array = 0;
                                switch (position) {
                                    case 1:
                                        array = R.array.rn_block_values;
                                        break;
                                    case 2:
                                        array = R.array.es_block_values;
                                        break;
                                    case 3:
                                        array = R.array.bo_block_values;
                                        break;
                                    case 4:
                                        array = R.array.dh_block_values;
                                        break;
                                    default:
                                        array = R.array.default_block_values;
                                }
                                //if (block_spinner.getAdapter().getCount()  0) {
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                        (IdentificationActivity.this, array,
                                                android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                block_spinner.setAdapter(adapter);
                            } else {
                                block_spinner.setEnabled(false);
                                // block_spinner.setEnabled(false);
                            }
                        }
                        else if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Madhya Pradesh")) {
                            if (position > 0) {
                                block_spinner.setEnabled(true);

                                int array = 0;
                                switch (position) {
                                    case 1:
                                        array = R.array.kh_block_values;
                                        break;
                                    case 2:
                                        array = R.array.rj_block_values;
                                        break;
                                    default:
                                        array = R.array.default_block_values;
                                }
                                //if (block_spinner.getAdapter().getCount()  0) {
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                        (IdentificationActivity.this, array,
                                                android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                block_spinner.setAdapter(adapter);
                            } else {
                                block_spinner.setEnabled(false);
                                // block_spinner.setEnabled(false);
                            }
                        }
                        //Sikkim State is selected & District item is selected for that Statte than do the following...
                        else if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Sikkim")) {
                            if (position > 0) {
                                block_spinner.setEnabled(true);

                                int array = 0;
                                switch (position) {
                                    case 1:
                                        array = R.array.sk_east_block_values;
                                        break;
                                    case 2:
                                        array = R.array.sk_west_block_values;
                                        break;
                                    case 3:
                                        array = R.array.sk_north_block_values;
                                        break;
                                    case 4:
                                        array = R.array.sk_south_block_values;
                                        break;
                                    default:
                                        array = R.array.default_block_values;
                                }

                                //if (block_spinner.getAdapter().getCount()  0) {
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                        (IdentificationActivity.this, array,
                                                android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                block_spinner.setAdapter(adapter);
                            } else {
                                block_spinner.setEnabled(false);
                                // block_spinner.setEnabled(false);
                            }
                        }
                        //Other State is selected & Other District item is selected for that Statte than do the following...
                        else if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                            if (position > 0) {
                                block_spinner.setEnabled(true);

                                int array = 0;
                                switch (position) {
                                    case 1:
                                        array = R.array.other_other_block_values;
                                        break;
                                    default:
                                        array = R.array.default_block_values;
                                }

                                //if (block_spinner.getAdapter().getCount()  0) {
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                                        (IdentificationActivity.this, array,
                                                android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                block_spinner.setAdapter(adapter);
                            } else {
                                block_spinner.setEnabled(false);
                                // block_spinner.setEnabled(false);
                            }
                        }

                        //end...

                        // }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }, 1000);


        et_tested_positive_date = findViewById(R.id.et_tested_positive_date);
        et_tested_positive_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {
                        getString(R.string.enter_number_of_days), getString(R.string.enter_date)
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View viewInflated = LayoutInflater.from(context).inflate(R.layout.input_tested_positive,
                                    findViewById(android.R.id.content), false);
                            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                            builder.setView(viewInflated);
                            builder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    et_tested_positive_date.setError(null);
                                    //passes number of days to this function to calculate the actual date...
                                    if(!input.getText().toString().isEmpty() || !input.getText().toString().equals("")) {
                                        String date = getDatefromDays(Integer.parseInt(input.getText().toString()));
                                        et_tested_positive_date.setText(date);
                                    }
                                    else {
                                        //do nothing close the dialog...
                                    }

                                }
                            });

                            builder.show();
                            input.requestFocus();

                        } else {

                            Calendar instance = Calendar.getInstance();
                            datePickerDialog = new DatePickerDialog(IdentificationActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                                    new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(0);
                                    cal.set(year, monthOfYear, dayOfMonth);
                                    Date date = cal.getTime();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    et_tested_positive_date.setText(simpleDateFormat.format(date));
                                }
                            }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                            //This will set the maxDate to Today only...
                            datePickerDialog.show();
                        }
                    }
                });
                builder.show();
            }
        });
        et_tested_positive_date.setText(patient1.getTestedPositiveDate());

        if (patientID_edit == null) {
            mobileNumberWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 10 && NetworkConnection.isOnline(IdentificationActivity.this)) {
                        checkUserExistsOrNot(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    /*if (s.length() == 10) {
                        checkUserExistsOrNot(s.toString());
                    }*/
                }
            };
            mPhoneNum.addTextChangedListener(mobileNumberWatcher);
        }

        //if medical advise enable the card visibility to input data
        if (isMedicalAdvice) {
            View llMedicalAdvice = findViewById(R.id.ll_medical_advice);
            llMedicalAdvice.setVisibility(View.VISIBLE);

            cbVaccineGuide = llMedicalAdvice.findViewById(R.id.cbVaccineGuide);
            cbCovidConcern = llMedicalAdvice.findViewById(R.id.cbCovidConcern);
            cbManagingBreathlessness = llMedicalAdvice.findViewById(R.id.cbManagingBreathlessness);
            cbManageVoiceIssue = llMedicalAdvice.findViewById(R.id.cbManageVoiceIssue);
            cbManageEating = llMedicalAdvice.findViewById(R.id.cbManageEating);
            cbDealProblems = llMedicalAdvice.findViewById(R.id.cbDealProblems);
            cbMentalHealth = llMedicalAdvice.findViewById(R.id.cbMentalHealth);
            cbExercises = llMedicalAdvice.findViewById(R.id.cbExercises);
            cbOthers = llMedicalAdvice.findViewById(R.id.cbOthers);
            et_medical_advice_extra = llMedicalAdvice.findViewById(R.id.et_medical_advice_extra);
            et_medical_advice_additional = llMedicalAdvice.findViewById(R.id.et_medical_advice_additional);
            cbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        et_medical_advice_extra.setEnabled(true);
                        et_medical_advice_extra.requestFocus();
                    } else {
                        et_medical_advice_extra.setText("");
                        et_medical_advice_extra.setEnabled(false);
                    }
                }
            });
        }
        chb_agree_privacy = findViewById(R.id.chb_agree_privacy);
        txt_privacy = findViewById(R.id.txt_privacy);
        txt_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyNotice_Activity.start(IdentificationActivity.this, true);
            }
        });
        if (!TextUtils.isEmpty(patientID_edit)) {
            findViewById(R.id.buttons).setVisibility(View.GONE);
        }
    }

    /**
     * @param dateString : number of days entered by the user is passed as an argument here.
     * @return date : formatted date string value is passed as a return value.
     */
    private String getDatefromDays(int dateString) {
        String date = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.forLanguageTag(sessionManager.getAppLanguage()));

        //number of days before date...
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -dateString);
        date = simpleDateFormat.format(calendar.getTime());
        Log.v("time", "todays date: " + date);
        //number of days calculation...

        return date;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mobileNumberWatcher != null) {
            mPhoneNum.removeTextChangedListener(mobileNumberWatcher);
        }
    }

/*
    public String getYear(int syear, int smonth, int sday, int eyear, int emonth, int eday) {
        String calculatedAge = null;
        int resmonth;
        int resyear;
        int resday;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            LocalDate today = LocalDate.now();
            LocalDate birthday = LocalDate.of(syear, smonth + 1, sday);

            Period p = Period.between(birthday, today);
            System.out.println(p.getDays());
            System.out.println(p.getMonths());
            System.out.println(p.getYears());
            calculatedAge = p.getYears() + " years  - " + p.getMonths() + " month - " + p.getDays() + " days";


        } else {

            //calculating year
            resyear = eyear - syear;

            //calculating month
            if (emonth >= smonth) {
                resmonth = emonth - smonth;
            } else {
                resmonth = emonth - smonth;
                resmonth = 12 + resmonth;
                resyear--;
            }

            //calculating date
            if (eday >= sday) {
                resday = eday - sday;
            } else {
                resday = eday - sday;
                resday = 30 + resday;
                if (resmonth == 0) {
                    resmonth = 11;
                    resyear--;
                } else {
                    resmonth--;
                }
            }

            //displaying error if calculated age is negative
            if (resday < 0 || resmonth < 0 || resyear < 0) {
                Toast.makeText(this, "Current Date must be greater than Date of Birth", Toast.LENGTH_LONG).show();
                mDOB.setError(getString(R.string.identification_screen_error_dob));
                mAge.setError(getString(R.string.identification_screen_error_age));
            } else {
                // t1.setText("Age: " + resyear + " years /" + resmonth + " months/" + resday + " days");

                calculatedAge = resyear + " years - " + resmonth + " months - " + resday + " days";
            }
        }

        return calculatedAge != null ? calculatedAge : " ";
    }
*/

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.identification_gender_male:
                if (checked)
                    mGender = "M";
                Log.v(TAG, "gender:" + mGender);
                break;
            case R.id.identification_gender_female:
                if (checked)
                    mGender = "F";
                Log.v(TAG, "gender:" + mGender);
                break;
        }
    }

    private InputFilter inputFilter_Name = new InputFilter() { //filter input for name fields
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            if (charSequence != null && BlockCharacterSet_Name.contains(("" + charSequence))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter inputFilter_Others = new InputFilter() { //filter input for all other fields
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            if (charSequence != null && BlockCharacterSet_Others.contains(("" + charSequence))) {
                return "";
            }
            return null;
        }
    };

    public void generateUuid() {

        patientUuid = uuidGenerator.UuidGenerator();

    }

    // This method is for setting the screen with existing values in database whenn user clicks edit details
    private void setscreen(String str) {
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();

        String patientSelection = "uuid=?";
        String[] patientArgs = {str};
        String[] patientColumns = {"uuid", "first_name", "middle_name", "last_name",
                "date_of_birth", "address1", "address2", "city_village", "state_province",
                "postal_code", "country", "phone_number", "gender", "sdw", "occupation", "patient_photo",
                "economic_status", "education_status", "caste"};
        Cursor idCursor = db.query("tbl_patient", patientColumns, patientSelection, patientArgs, null, null, null);
        if (idCursor.moveToFirst()) {
            do {
                patient1.setUuid(idCursor.getString(idCursor.getColumnIndexOrThrow("uuid")));
                patient1.setFirst_name(idCursor.getString(idCursor.getColumnIndexOrThrow("first_name")));
                patient1.setMiddle_name(idCursor.getString(idCursor.getColumnIndexOrThrow("middle_name")));
                patient1.setLast_name(idCursor.getString(idCursor.getColumnIndexOrThrow("last_name")));
                patient1.setDate_of_birth(idCursor.getString(idCursor.getColumnIndexOrThrow("date_of_birth")));
                patient1.setAddress1(idCursor.getString(idCursor.getColumnIndexOrThrow("address1")));
                patient1.setAddress2(idCursor.getString(idCursor.getColumnIndexOrThrow("address2")));
                patient1.setCity_village(idCursor.getString(idCursor.getColumnIndexOrThrow("city_village")));
                patient1.setState_province(idCursor.getString(idCursor.getColumnIndexOrThrow("state_province")));
                patient1.setPostal_code(idCursor.getString(idCursor.getColumnIndexOrThrow("postal_code")));
                patient1.setCountry(idCursor.getString(idCursor.getColumnIndexOrThrow("country")));
                patient1.setPhone_number(idCursor.getString(idCursor.getColumnIndexOrThrow("phone_number")));
                patient1.setGender(idCursor.getString(idCursor.getColumnIndexOrThrow("gender")));
                patient1.setSdw(idCursor.getString(idCursor.getColumnIndexOrThrow("sdw")));
                patient1.setPatient_photo(idCursor.getString(idCursor.getColumnIndexOrThrow("patient_photo")));
                patient1.setTestedPositiveDate(idCursor.getString(idCursor.getColumnIndexOrThrow("occupation")));
//                patient1.setBank_account(idCursor.getString(idCursor.getColumnIndexOrThrow("Bank Account")));
//                patient1.setMobile_type(idCursor.getString(idCursor.getColumnIndexOrThrow("Mobile Phone Type")));
//                patient1.setWhatsapp_mobile(idCursor.getString(idCursor.getColumnIndexOrThrow("Use WhatsApp")));
//                patient1.setNo_of_family_members(idCursor.getString(idCursor.getColumnIndexOrThrow("Total Family Members")));
//                patient1.setNo_of_family_currently_live(idCursor.getString(idCursor.getColumnIndexOrThrow("Total Family Members Staying")));
//                patient1.setSource_of_water(idCursor.getString(idCursor.getColumnIndexOrThrow("Drinking Water Source")));
//                patient1.setWater_safe(idCursor.getString(idCursor.getColumnIndexOrThrow("Safe Drinking Water")));
//                patient1.setTime_travel_water(idCursor.getString(idCursor.getColumnIndexOrThrow("Time Drinking Water Source")));
//                patient1.setWater_availability(idCursor.getString(idCursor.getColumnIndexOrThrow("Drinking Water Availability")));
//                patient1.setToilet_facility(idCursor.getString(idCursor.getColumnIndexOrThrow("Toilet Facility")));
//                patient1.setStructure_house(idCursor.getString(idCursor.getColumnIndexOrThrow("House Structure")));
//                patient1.setHectars_land(idCursor.getString(idCursor.getColumnIndexOrThrow("Family Cultivable Land")));

            } while (idCursor.moveToNext());
            idCursor.close();
        }
        String patientSelection1 = "patientuuid = ?";
        String[] patientArgs1 = {str};
        String[] patientColumns1 = {"value", "person_attribute_type_uuid"};
        final Cursor idCursor1 = db.query("tbl_patient_attribute", patientColumns1, patientSelection1, patientArgs1, null, null, null);
        String name = "";
        if (idCursor1.moveToFirst()) {
            do {
                try {
                    name = patientsDAO.getAttributesName(idCursor1.getString(idCursor1.getColumnIndexOrThrow("person_attribute_type_uuid")));
                } catch (DAOException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

                if (name.equalsIgnoreCase("caste")) {
                    patient1.setCaste(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Telephone Number")) {
                    patient1.setPhone_number(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Education Level")) {
                    patient1.setEducation_level(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Economic Status")) {
                    patient1.setEconomic_status(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Son/wife/daughter")) {
                    patient1.setSdw(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("positiveDate")) {
                    patient1.setTestedPositiveDate(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
              /*
                if (name.equalsIgnoreCase("Bank Account")) {
                    patient1.setBank_account(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Mobile Phone Type")) {
                    patient1.setMobile_type(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Use WhatsApp")) {
                    patient1.setWhatsapp_mobile(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Total Family Members")) {
                    patient1.setNo_of_family_members(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Total Family Members Staying")) {
                    patient1.setNo_of_family_currently_live(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Drinking Water Source")) {
                    patient1.setSource_of_water(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Safe Drinking Water")) {
                    patient1.setWater_safe(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Time Drinking Water Source")) {
                    patient1.setTime_travel_water(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Drinking Water Availability")) {
                    patient1.setWater_availability(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Toilet Facility")) {
                    patient1.setToilet_facility(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("House Structure")) {
                    patient1.setStructure_house(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Family Cultivable Land")) {
                    patient1.setHectars_land(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }*/

            } while (idCursor1.moveToNext());
        }
        idCursor1.close();

    }


    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(this);
        alertdialogBuilder.setMessage(R.string.are_you_want_go_back);
        alertdialogBuilder.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent i_back = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i_back);
            }
        });
        alertdialogBuilder.setNegativeButton(R.string.generic_no, null);

        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //negativeButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);
    }

    public void showAlertDialogButtonClicked(String errorMessage) {

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setTitle("Config Error");
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setNeutralButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent i = new Intent(IdentificationActivity.this, SetupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// This flag ensures all activities on top of the CloseAllViewsDemo are cleared.
                startActivity(i);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "Result Received");
        if (requestCode == CameraActivity.TAKE_IMAGE) {
            Log.v(TAG, "Request Code " + CameraActivity.TAKE_IMAGE);
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Result OK");
                mCurrentPhotoPath = data.getStringExtra("RESULT");
                Log.v("IdentificationActivity", mCurrentPhotoPath);

/*
                Glide.with(this)
                        .load(new File(mCurrentPhotoPath))
                        .thumbnail(0.25f)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mImageView);
*/
            }
        }
    }

    public void onPatientCreateClicked(String personUUID) {
        String birthDate = "";
        PatientsDAO patientsDAO = new PatientsDAO();
        PatientAttributesDTO patientAttributesDTO = new PatientAttributesDTO();
        List<PatientAttributesDTO> patientAttributesDTOList = new ArrayList<>();
//        uuid = UUID.randomUUID().toString();
        uuid = personUUID;

        patientdto.setUuid(uuid);
        Gson gson = new Gson();

        boolean cancel = false;
        View focusView = null;


/*
        if (dob.equals("") || dob.toString().equals("")) {
            if (dob.after(today)) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
                alertDialogBuilder.setTitle(R.string.error);
                alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_dob);
                //alertDialogBuilder.setMessage(getString(R.string.identification_dialog_date_error));
                alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();

                mDOBPicker.show();
                alertDialog.show();

                Button postiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                postiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                // postiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);
                return;
            }
        }
*/

        if (mPhoneNum.getText().toString().trim().length() > 0) {
            if (mPhoneNum.getText().toString().trim().length() < 10) {
                mPhoneNum.requestFocus();
                mPhoneNum.setError(getString(R.string.enter_10_digits));
                return;
            }
        }

   /*     ArrayList<EditText> values = new ArrayList<>();
        values.add(mFirstName);
        values.add(mMiddleName);
        values.add(mLastName);
        values.add(mDOB);
        values.add(mPhoneNum);
        values.add(mAddress1);
        values.add(mAddress2);
        values.add(mCity);
        values.add(mPostal);
        values.add(mRelationship);
        values.add(mOccupation);*/

/*
        if (!mGenderF.isChecked() && !mGenderM.isChecked()) {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
            alertDialogBuilder.setTitle(R.string.error);
            alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_gender);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            return;
        }
*/

        if (!mFirstName.getText().toString().equals("") && !mLastName.getText().toString().equals("")
                && !countryText.getText().toString().equals("") &&
                !autocompleteState.getText().toString().equals("") &&
                !mAge.getText().toString().equals("") && !mPhoneNum.getText().toString().equals("")
                && (mGenderF.isChecked() || mGenderM.isChecked())) {

            //passes number of days to this function to calculate the actual date...
            if(!mAge.getText().toString().isEmpty() || !mAge.getText().toString().equals("")) {
                String age = getYearFromAge(Integer.parseInt(mAge.getText().toString()));
                birthDate = DateAndTimeUtils.getFormatedDateOfBirth(StringUtils.getValue(age));
                Log.v("age", "birthdate: "+birthDate);
            }
            else {
                //do nothing close the dialog...
            }

            Log.v(TAG, "Result");

        } else {
            if (mFirstName.getText().toString().equals("")) {
                mFirstName.setError(getString(R.string.error_field_required));
            }

            if (mLastName.getText().toString().equals("")) {
                mLastName.setError(getString(R.string.error_field_required));
            }

//            if (mDOB.getText().toString().equals("")) {
//                mDOB.setError(getString(R.string.error_field_required));
//            }

            if (mAge.getText().toString().equals("")) {
                mAge.setError(getString(R.string.error_field_required));
            }


            if (mPhoneNum.getText().toString().equals("")) {
                mPhoneNum.setError(getString(R.string.error_field_required));
            }

            /*if (et_tested_positive_date.getText().toString().equals("")) {
                et_tested_positive_date.setError(getString(R.string.error_field_required));
            }*/

            if (autocompleteState.getText().toString().equals("")) {
                autocompleteState.setError(getString(R.string.error_field_required));
            }


//            if (mCity.getText().toString().equals("")) {
//                mCity.setError(getString(R.string.error_field_required));
//            }

            if (!mGenderF.isChecked() && !mGenderM.isChecked()) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
                alertDialogBuilder.setTitle(R.string.error);
                alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_gender);
                alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);

            }


            Toast.makeText(IdentificationActivity.this, R.string.identification_screen_required_fields, Toast.LENGTH_LONG).show();
            return;
        }

        if (mCountry.getSelectedItemPosition() == 0) {
            countryText.setError(getString(R.string.error_field_required));
            focusView = countryText;
            cancel = true;
            return;
        } else {
            countryText.setError(null);
        }


        /*if (autocompleteState.getText().toString().equalsIgnoreCase("")) {
            autocompleteState.setError(getString(R.string.error_field_required));
            focusView = autocompleteState;
            cancel = true;
            return;
        } else {
            autocompleteState.setError(null);
        }*/

        if (state_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (city_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (block_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (isUserExists) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IdentificationActivity.this);
            alertDialogBuilder.setMessage(R.string.warning_user_exist);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        // TODO: Add validations for all Spinners here...
     /*   if (occupation_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) occupation_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = occupation_spinner;
            cancel = true;
            return;
        }*/

/*
        if (occupation_edittext.getVisibility() == View.VISIBLE && occupation_edittext.getText().toString().isEmpty() &&
                occupation_edittext.getText().toString().equalsIgnoreCase("") &&
                occupation_edittext.getText().toString() != null) {

            occupation_edittext.setError(getString(R.string.select));
            focusView = occupation_edittext;
            cancel = true;
            return;
        }
*/

/*
        if (bankaccount_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) bankaccount_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = bankaccount_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (mobilephone_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) mobilephone_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = mobilephone_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (whatsapp_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) whatsapp_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = whatsapp_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (familyhead_checkbox.isChecked()) {

            if (no_of_member_edittext.getText().toString().equalsIgnoreCase("") &&
                    no_of_member_edittext.getText().toString().isEmpty()) {
                no_of_member_edittext.setError(getString(R.string.select));
                // no_of_member_edittext.setTextColor(Color.RED);
                focusView = no_of_member_edittext;
                cancel = true;
                return;
            }

            if (no_of_staying_members_edittext.getText().toString().equalsIgnoreCase("") &&
                    no_of_staying_members_edittext.getText().toString().isEmpty()) {
                no_of_staying_members_edittext.setError(getString(R.string.select));
                // no_of_staying_members_edittext.setTextColor(Color.RED);
                focusView = no_of_staying_members_edittext;
                cancel = true;
                return;
            }

            if (source_of_water_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) source_of_water_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = source_of_water_spinner;
                cancel = true;
                return;
            }

            if (howtomake_water_safe_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) howtomake_water_safe_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = howtomake_water_safe_spinner;
                cancel = true;
                return;
            }

            if (watersafe_edittext.getVisibility() == View.VISIBLE && watersafe_edittext.getText().toString().isEmpty() &&
                    watersafe_edittext.getText().toString().equalsIgnoreCase("") &&
                    watersafe_edittext.getText().toString() != null) {

                watersafe_edittext.setError(getString(R.string.select));
                focusView = watersafe_edittext;
                cancel = true;
                return;
            }

            if (!time_water_checkbox.isChecked() && time_water_editText.getText().toString().isEmpty() &&
                    time_water_editText.getText().toString().equalsIgnoreCase("")) {
                //checks if both the fields are not selected...
                time_water_checkbox.setError(getString(R.string.select));

                focusView = time_water_checkbox;
                focusView = time_water_editText;
                cancel = true;
                return;
            }

//                if(time_water_checkbox.isChecked() && time_water_editText.getText().toString().isEmpty() &&
//                time_water_editText.getText().toString().equalsIgnoreCase("")) {
//                    //checks that checkbox is checked but editTExt is empty...
//                    time_water_editText.setError("Select");
//                    time_water_editText.setTextColor(Color.RED);
//                    focusView = time_water_editText;
//                    cancel = true;
//                    return;
//                }

            if (water_availability_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) water_availability_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = water_availability_spinner;
                cancel = true;
                return;
            }

            if (toilet_facility_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) toilet_facility_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = toilet_facility_spinner;
                cancel = true;
                return;
            }

            if (toiletfacility_edittext.getVisibility() == View.VISIBLE && toiletfacility_edittext.getText().toString().isEmpty() &&
                    toiletfacility_edittext.getText().toString().equalsIgnoreCase("") &&
                    toiletfacility_edittext.getText().toString() != null) {

                toiletfacility_edittext.setError(getString(R.string.select));
                focusView = toiletfacility_edittext;
                cancel = true;
                return;
            }

            if (structure_of_house_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) structure_of_house_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = structure_of_house_spinner;
                cancel = true;
                return;
            }

            if (!hectars_land_checkbox.isChecked() && hectars_land_editText.getText().toString().isEmpty() &&
                    hectars_land_editText.getText().toString().equalsIgnoreCase("")) {
                //checks if both the fields are not selected...
                hectars_land_checkbox.setError(getString(R.string.select));

                focusView = hectars_land_checkbox;
                focusView = hectars_land_editText;
                cancel = true;
                return;

            }


//                if(hectars_land_checkbox.isChecked() && hectars_land_editText.getText().toString().isEmpty() &&
//                        hectars_land_editText.getText().toString().equalsIgnoreCase("")) {
//                    //checks that checkbox is checked but editTExt is empty...
//                    hectars_land_editText.setError("Select");
//                    hectars_land_editText.setTextColor(Color.RED);
//                    focusView = hectars_land_editText;
//                    cancel = true;
//                    return;
//                }

        }
*/


        if (cancel) {
            focusView.requestFocus();
        } else {

            patientdto.setFirstname(StringUtils.getValue(mFirstName.getText().toString()));
            patientdto.setMiddlename(StringUtils.getValue(mMiddleName.getText().toString()));
            patientdto.setLastname(StringUtils.getValue(mLastName.getText().toString()));
            patientdto.setPhonenumber(StringUtils.getValue(mPhoneNum.getText().toString()));
            patientdto.setGender(StringUtils.getValue(mGender));
            patientdto.setTestedPositiveDate(StringUtils.getValue(et_tested_positive_date.getText().toString()));

            // String dob = StringUtils.hi_or__en(mDOB.getText().toString(), month_index);
           /* String[] dob_array = mDOB.getText().toString().split(" ");
            Log.d("dob_array", "0: " + dob_array[0]);
            Log.d("dob_array", "0: " + dob_array[1]);
            Log.d("dob_array", "0: " + dob_array[2]);*/

            //get month index and return English value for month.
        /*    if (dob_indexValue == 15) { //no value has been edited...ie. DOB/Age is not selected...
                String dob = StringUtils.hi_or_as__en_noEdit
                        (mDOB.getText().toString(), sessionManager.getAppLanguage());
                patientdto.setDateofbirth(DateAndTimeUtils.getFormatedDateOfBirth
                        (StringUtils.getValue(dob)));
            } else {
                String dob = StringUtils.hi_or_as__en_month(dob_indexValue);
                dob_array[1] = dob_array[1].replace(dob_array[1], dob);
                String dob_value = dob_array[0] + " " + dob_array[1] + " " + dob_array[2];

                patientdto.setDateofbirth(DateAndTimeUtils.getFormatedDateOfBirth
                        (StringUtils.getValue(dob_value)));

            }*/

           /*  patientdto.setDateofbirth(DateAndTimeUtils.
                     getFormatedDateOfBirth(StringUtils.getValue(dob_value)));*/
            patientdto.setDateofbirth(birthDate);

            patientdto.setAddress1(StringUtils.getValue(mAddress1.getText().toString()));
            patientdto.setAddress2(StringUtils.getValue(mAddress2.getText().toString()));
            patientdto.setCityvillage(StringUtils.getValue(mCity.getText().toString()));
            patientdto.setPostalcode(StringUtils.getValue(mPostal.getText().toString()));
            patientdto.setCountry(StringUtils.getValue(mCountry.getSelectedItem().toString()));
            patientdto.setPatientPhoto(mCurrentPhotoPath);
//          patientdto.setEconomic(StringUtils.getValue(m));
            patientdto.setStateprovince(StringUtils.getValue(autocompleteState.getText().toString()));
            patientdto.setCityvillage(StringUtils.getValue(city_spinner.getSelectedItem().toString()));

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Education Level"));
            patientAttributesDTO.setValue(block_spinner.getSelectedItem().toString().equalsIgnoreCase("Select") ? "" : block_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("caste"));
//            patientAttributesDTO.setValue(StringUtils.getProvided(mCaste));
            patientAttributesDTO.setValue("nurse_app");
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("creatoruuid"));
//            patientAttributesDTO.setValue(StringUtils.getProvided(mCaste));
            patientAttributesDTO.setValue(sessionManager.getProviderID());
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Telephone Number"));
            patientAttributesDTO.setValue(StringUtils.getValue(mPhoneNum.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Son/wife/daughter"));
            patientAttributesDTO.setValue(StringUtils.getValue(mRelationship.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("positiveDate"));
            patientAttributesDTO.setValue(StringUtils.getValue(et_tested_positive_date.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Economic Status"));
            patientAttributesDTO.setValue(StringUtils.getProvided(mEconomicStatus));
            patientAttributesDTOList.add(patientAttributesDTO);

            /*patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Education Level"));
            patientAttributesDTO.setValue(StringUtils.getProvided(mEducation));
            patientAttributesDTOList.add(patientAttributesDTO);*/

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("ProfileImageTimestamp"));
            patientAttributesDTO.setValue(AppConstants.dateAndTimeUtils.currentDateTime());

            //House Hold Registration
/*
            if (sessionManager.getHouseholdUuid().equals("")) {

                String HouseHold_UUID = UUID.randomUUID().toString();
                sessionManager.setHouseholdUuid(HouseHold_UUID);

                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("householdID"));
                patientAttributesDTO.setValue(HouseHold_UUID);
                patientAttributesDTOList.add(patientAttributesDTO);

            } else {

                String HouseHold_UUID = sessionManager.getHouseholdUuid();
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("householdID"));
                patientAttributesDTO.setValue(HouseHold_UUID);
                patientAttributesDTOList.add(patientAttributesDTO);

            }
*/

            //Occupation ...
         /*   if (occupation_edittext.getVisibility() == View.VISIBLE && !occupation_edittext.getText().toString().isEmpty() &&
                    !occupation_edittext.getText().toString().equalsIgnoreCase("")) {
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("occupation"));
                patientAttributesDTO.setValue(StringUtils.getValue(occupation_edittext.getText().toString()));
                Log.d("HOH", "Occupation: " + occupation_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);
            } else {
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("occupation"));
                //patientAttributesDTO.setValue(occupation_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(occupation_spinner));
                Log.d("HOH", "Occupation: " + occupation_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);
            }*/

            //Bank Account...
          /*  patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Bank Account"));
            //  patientAttributesDTO.setValue(bankaccount_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(bankaccount_spinner));
            Log.d("HOH", "Bankacc: " + bankaccount_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            //Mobile Phone own...
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Mobile Phone Type"));
            // patientAttributesDTO.setValue(mobilephone_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(mobilephone_spinner));
            Log.d("HOH", "mobile phone type: " + mobilephone_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            //WhatsApp Family member ...
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Use WhatsApp"));
            // patientAttributesDTO.setValue(whatsapp_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(whatsapp_spinner));
            Log.d("HOH", "Whatsapp use: " + whatsapp_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);
*/
            //Check first if Are you Head of Household checkbox is checked or not...
/*            if (familyhead_checkbox.isChecked()) {
                //Total no of members in household  ...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Total Family Members"));
                patientAttributesDTO.setValue(StringUtils.getValue(no_of_member_edittext.getText().toString()));
                Log.d("HOH", "total family meme: " + no_of_member_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Total no of members living in household  ...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Total Family Members Staying"));
                patientAttributesDTO.setValue(StringUtils.getValue(no_of_staying_members_edittext.getText().toString()));
                Log.d("HOH", "Total family stay: " + no_of_staying_members_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Main source of drinking water...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Drinking Water Source"));
                // patientAttributesDTO.setValue(source_of_water_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(source_of_water_spinner));
                Log.d("HOH", "source of water: " + source_of_water_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //How do you make Drinking water Safe?
                if (watersafe_edittext.getVisibility() == View.VISIBLE && !watersafe_edittext.getText().toString()
                        .isEmpty() && !watersafe_edittext.getText().toString().equalsIgnoreCase("")) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Safe Drinking Water"));
                    patientAttributesDTO.setValue(StringUtils.getValue(watersafe_edittext.getText().toString()));
                    Log.d("HOH", "water safe: " + watersafe_edittext.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO); //edit text is visible...
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Safe Drinking Water"));
                    // patientAttributesDTO.setValue(howtomake_water_safe_spinner.getSelectedItem().toString());
                    patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(howtomake_water_safe_spinner));
                    Log.d("HOH", "water safe: " + howtomake_water_safe_spinner.getSelectedItem().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                // Time taken to get water...
                if (time_water_checkbox.isChecked()) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Time Drinking Water Source"));
                    // patientAttributesDTO.setValue(StringUtils.getValue(time_water_checkbox.getText().toString()));
                    patientAttributesDTO.setValue(StringUtils.getCheckbox_Hi_En_Hi(time_water_checkbox.getText().toString())); //hi to en and vice-versa...
                    Log.d("HOH", "time to bring water:create " + time_water_checkbox.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    //User enters value here...
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Time Drinking Water Source"));
                    patientAttributesDTO.setValue(StringUtils.getValue(time_water_editText.getText().toString()));
                    Log.d("HOH", "time to bring water value entered: " + time_water_editText.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                //            patientAttributesDTO = new PatientAttributesDTO();
//            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
//            patientAttributesDTO.setPatientuuid(uuid);
//            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
//                    .getUuidForAttribute("Time Drinking Water Source"));
//            patientAttributesDTO.setValue(howtomake_water_safe_spinner.getSelectedItem().toString());
//            patientAttributesDTOList.add(patientAttributesDTO);

                //Drinking water availability...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Drinking Water Availability"));
                // patientAttributesDTO.setValue(water_availability_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(water_availability_spinner));
                Log.d("HOH", "Water availability: " + water_availability_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Toilet Facility...
                if (toiletfacility_edittext.getVisibility() == View.VISIBLE && !toiletfacility_edittext
                        .getText().toString().isEmpty() && !toiletfacility_edittext.getText()
                        .toString().equalsIgnoreCase("")) {

                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Toilet Facility"));
                    patientAttributesDTO.setValue(toiletfacility_edittext.getText().toString());
                    Log.d("HOH", "Toilet: " + toiletfacility_edittext.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Toilet Facility"));
                    //  patientAttributesDTO.setValue(toilet_facility_spinner.getSelectedItem().toString());
                    patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(toilet_facility_spinner));
                    Log.d("HOH", "Toilet: " + toilet_facility_spinner.getSelectedItem().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                //House Structure...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("House Structure"));
                //  patientAttributesDTO.setValue(structure_of_house_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(structure_of_house_spinner));
                Log.d("HOH", "Structure: " + structure_of_house_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                // Hectars of Land...
                if (hectars_land_checkbox.isChecked()) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Family Cultivable Land"));
                    //  patientAttributesDTO.setValue(StringUtils.getValue(hectars_land_checkbox.getText().toString()));
                    patientAttributesDTO.setValue(StringUtils.getCheckbox_Hi_En_Hi(hectars_land_checkbox.getText().toString()));
                    Log.d("HOH", "Hectars: " + hectars_land_checkbox.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Family Cultivable Land"));
                    patientAttributesDTO.setValue(StringUtils.getValue(hectars_land_editText.getText().toString()));
                    Log.d("HOH", "Hectars value entered: " + hectars_land_editText.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);

                }

                // sessionManager.setHOH_checkbox(true);
                // Log.d("session", "session_create: " + sessionManager.getHOH_checkbox());

            }*/
           /* else {
                // sessionManager.setHOH_checkbox(false);
                //  Log.d("session", "session_create: " + sessionManager.getHOH_checkbox());
            }*/
            //end of checking if the family head checkbox is checked or not...

            //            patientAttributesDTO = new PatientAttributesDTO();
//            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
//            patientAttributesDTO.setPatientuuid(uuid);
//            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
//                    .getUuidForAttribute("Family Cultivable Land"));
//            patientAttributesDTO.setValue(howtomake_water_safe_spinner.getSelectedItem().toString());
//            patientAttributesDTOList.add(patientAttributesDTO);


            patientAttributesDTOList.add(patientAttributesDTO);
            Logger.logD(TAG, "PatientAttribute list size" + patientAttributesDTOList.size());
            patientdto.setPatientAttributesDTOList(patientAttributesDTOList);
            patientdto.setSyncd(false);
            Logger.logD("patient json : ", "Json : " + gson.toJson(patientdto, PatientDTO.class));

        }

        try {
            Logger.logD(TAG, "insertpatinet ");
            boolean isPatientInserted = patientsDAO.insertPatientToDB(patientdto, uuid);
            boolean isPatientImageInserted = imagesDAO.insertPatientProfileImages(mCurrentPhotoPath, uuid);

            if (NetworkConnection.isOnline(getApplication())) {
//                patientApiCall();
//                frameJson();

//                AppConstants.notificationUtils.showNotifications(getString(R.string.patient_data_upload),
//                        getString(R.string.uploading) + patientdto.getFirstname() + "" + patientdto.getLastname() +
//                                "'s data", 2, getApplication());

               /* SyncDAO syncDAO = new SyncDAO();
                ImagesPushDAO imagesPushDAO = new ImagesPushDAO();
                boolean push = syncDAO.pushDataApi();
                boolean pushImage = imagesPushDAO.patientProfileImagesPush();*/

            }

            if (isPatientInserted && isPatientImageInserted) {
                if (isMedicalAdvice) {
                    //if from medical  advise option then create medical advice visit first(automatically)
                    createMedicalAdviceVisit();
                }
                else {
                    SyncDAO syncDAO = new SyncDAO();
                    ImagesPushDAO imagesPushDAO = new ImagesPushDAO();
                    boolean push = syncDAO.pushDataApi();
                    boolean pushImage = imagesPushDAO.patientProfileImagesPush();
                }

                Logger.logD(TAG, "inserted");
                Intent i = new Intent(getApplication(), PatientDetailActivity.class);
                i.putExtra("patientUuid", uuid);
                i.putExtra("patientName", patientdto.getFirstname() + " " + patientdto.getLastname());
                i.putExtra("tag", "newPatient");
                i.putExtra("privacy", privacy_value);
                i.putExtra("hasPrescription", "false");
                Log.d(TAG, "Privacy Value on (Identification): " + privacy_value); //privacy value transferred to PatientDetail activity.
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(i);
            } else {
                Toast.makeText(IdentificationActivity.this, "Error of adding the data", Toast.LENGTH_SHORT).show();
            }
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    private void registerUser() {

        if (mFirstName.getText().toString().equals("")) {
            mFirstName.setError(getString(R.string.error_field_required));
            return;
        }
        if (mLastName.getText().toString().equals("")) {
            mLastName.setError(getString(R.string.error_field_required));
            return;
        }
        if (mAge.getText().toString().equals("")) {
            mAge.setError(getString(R.string.error_field_required));
            return;
        }
        if (!mGenderF.isChecked() && !mGenderM.isChecked()) {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
            alertDialogBuilder.setTitle(R.string.error);
            alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_gender);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);
            return;
        }

        if (mPhoneNum.getText().toString().equals("")) {
            mPhoneNum.setError(getString(R.string.error_field_required));
            return;
        }

        if (mPhoneNum.getText().toString().trim().length() > 0) {
            if (mPhoneNum.getText().toString().trim().length() < 10) {
                mPhoneNum.requestFocus();
                mPhoneNum.setError(getString(R.string.enter_10_digits));
                return;
            }
        }

        /*if (et_tested_positive_date.getText().toString().equals("")) {
            et_tested_positive_date.setError(getString(R.string.error_field_required));
            return;
        }*/

        if (state_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (city_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (block_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (isUserExists) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IdentificationActivity.this);
            alertDialogBuilder.setMessage(R.string.warning_user_exist);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        if (!checkUserCallExecuted) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IdentificationActivity.this);
            alertDialogBuilder.setMessage(R.string.try_again_later);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mPhoneNum.setText("");
                    mPhoneNum.requestFocus();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        if (isMedicalAdvice
            && !cbCovidConcern.isChecked()
            && !cbVaccineGuide.isChecked()
            && !cbCovidConcern.isChecked()
            && !cbManagingBreathlessness.isChecked()
            && !cbManageVoiceIssue.isChecked()
            && !cbManageEating.isChecked()
            && !cbDealProblems.isChecked()
            && !cbMentalHealth.isChecked()
            && !cbExercises.isChecked()
            && !cbOthers.isChecked()
            && TextUtils.isEmpty(et_medical_advice_additional.getText())) {
                Toast.makeText(context, R.string.error_medical_visit_data, Toast.LENGTH_SHORT).show();
                return;
        }

        if (isMedicalAdvice && cbOthers.isChecked() && TextUtils.isEmpty(et_medical_advice_extra.getText())) {
            Toast.makeText(context, R.string.error_medical_visit_data, Toast.LENGTH_SHORT).show();
            return;
        }

        //check if privacy notice is checked
        if (TextUtils.isEmpty(patientID_edit) && !chb_agree_privacy.isChecked()) {
            Toast.makeText(context, getString(R.string.please_read_out_privacy_consent_first), Toast.LENGTH_SHORT).show();
            return;
        }

        String randomString = generatePassword(8);

        ///////////Data Model for step 1
        UserCreationData userCreationData = new UserCreationData();
        userCreationData.setUsername("" + mPhoneNum.getText().toString());
        userCreationData.setPassword("" + randomString);

        Log.e("Password", "" + randomString);

        NameUser nameUser = new NameUser();
        nameUser.setGivenName("" + mFirstName.getText().toString());
        nameUser.setMiddleName("" + mMiddleName.getText().toString());
        nameUser.setFamilyName("" + mLastName.getText().toString());

        PersonUser personUser = new PersonUser();
        List<NameUser> nameUserList = new ArrayList<>();
        nameUserList.add(nameUser);
        personUser.setNames(nameUserList);

        personUser.setGender("" + mGender);

        List<String> roles = new ArrayList<>();
        roles.add("8d94f280-c2cc-11de-8d13-0010c6dffd0f");
        roles.add("f4c6152c-50cf-4055-b842-557baa0c5e30");
        userCreationData.setRoles(roles);

        userCreationData.setPerson(personUser);

        Gson gson = new Gson();
        Log.e("JSON-STEP1- ", "" + gson.toJson(userCreationData));

////////////////////API Call

        UrlModifiers urlModifiers = new UrlModifiers();
        String urlString = urlModifiers.loginUrl(sessionManager.getServerUrl());
        encoded = base64Utils.encoded("admin", BuildConfig.DEBUG ? "IHUser#1" : "IHUser#1");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Observable<LoginModel> loginModelObservable = AppConstants.apiInterface.LOGIN_MODEL_OBSERVABLE(urlString, "Basic " + encoded);
        loginModelObservable.subscribe(new Observer<LoginModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LoginModel loginModel) {
                int responsCode = loginModel.hashCode();
                Boolean authencated = loginModel.getAuthenticated();
                Gson gson = new Gson();
                Logger.logD(TAG, "success" + gson.toJson(loginModel));
//                sessionManager.setChwname(loginModel.getUser().getDisplay());
//                sessionManager.setCreatorID(loginModel.getUser().getUuid());
//                sessionManager.setSessionID(loginModel.getSessionId());
//                sessionManager.setProviderID(loginModel.getUser().getPerson().getUuid());

                UrlModifiers urlModifiers = new UrlModifiers();
                String url = urlModifiers.setRegistrationURL();
                if (authencated) {
                    Observable<UserCallRes> resultsObservable = AppConstants.apiInterface.REGISTER_USER(url, "Basic " + encoded, userCreationData);
                    resultsObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<UserCallRes>() {
                                @Override
                                public void onNext(UserCallRes res) {
                                    if (res != null) {
//                                        Log.e("UUID", "" + res.getPerson().getUuid());

//                                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

//                                        encoded = "";
//                                        encoded = base64Utils.encoded(userName, password);
//                                        sessionManager.setEncoded(encoded);

                                        //Commented by Venu For Account Manager Issue.
                                      /*  final Account account = new Account(userName, "io.intelehealth.openmrs");
                                        manager.addAccountExplicitly(account, password, null);*/

//                                        sessionManager.setLocationName("" + userAddressData.getCountyDistrict());
//                                        sessionManager.setLocationUuid("b56d5d16-bf89-4ac0-918d-e830fbfba290");
//                                        sessionManager.setLocationDescription("In Maharashtra State");
//                                        sessionManager.setServerUrl(BuildConfig.CLEAN_URL);
//                                        sessionManager.setServerUrlRest("https://" + BuildConfig.CLEAN_URL + "/openmrs/ws/rest/v1/");
//                                        sessionManager.setServerUrlBase("https://" + BuildConfig.CLEAN_URL + "/openmrs");
//                                        sessionManager.setBaseUrl("https://" + BuildConfig.CLEAN_URL + "/openmrs/ws/rest/v1/");
//                                        sessionManager.setSetupComplete(true);

//                                        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
//                                                .applicationId(AppConstants.IMAGE_APP_ID)
//                                                .server("https://" + BuildConfig.CLEAN_URL + "/parse/")
//                                                .build()
//                                        );


                                        /*personUUID = res.getPerson().getUuid();
                                        onPatientCreateClicked(personUUID);*/
                                        personUUID = res.getPerson().getUuid();
                                        createUserMapping(personUUID, mPhoneNum.getText().toString(), randomString);

//                                        sendBirthDataCall(encoded, personUUID, userBirthData, userAddressData);

                                    } else {
                                        Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    progress.dismiss();
                                    Log.e("LOCATIONS", "" + e);
                                    Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onComplete() {
                                    Logger.logD(TAG, "completed");
                                }
                            });
                }
            }

            @Override
            public void onError(Throwable e) {
//                progress.dismiss();
                Logger.logD(TAG, "Login Failure" + e.getMessage());
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
                Logger.logD(TAG, "completed");
            }
        });
    }

    private String generatePassword(int length) {
//        char[] SYMBOLS = "-/.^&*_!@%=+>)".toCharArray();
        char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] NUMBERS = "0123456789".toCharArray();
        char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random rand = new SecureRandom();

        char[] password = new char[length];
        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
//        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 3; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        return new String(password);
    }

    public void onPatientUpdateClicked(Patient patientdto) {
        String birthDate = "";
        PatientsDAO patientsDAO = new PatientsDAO();
        PatientAttributesDTO patientAttributesDTO = new PatientAttributesDTO();
        List<PatientAttributesDTO> patientAttributesDTOList = new ArrayList<>();
        uuid = patientdto.getUuid();

        patientdto.setUuid(uuid);
        Gson gson = new Gson();

        boolean cancel = false;
        View focusView = null;


/*
        if (dob.equals("") || dob.toString().equals("")) {
            if (dob.after(today)) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
                alertDialogBuilder.setTitle(R.string.error);
                alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_dob);
                //alertDialogBuilder.setMessage(getString(R.string.identification_dialog_date_error));
                alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();

                mDOBPicker.show();
                alertDialog.show();

                Button postiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                postiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                // postiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);
                return;
            }
        }
*/

        if (mPhoneNum.getText().toString().trim().length() > 0) {
            if (mPhoneNum.getText().toString().trim().length() < 10) {
                mPhoneNum.requestFocus();
                mPhoneNum.setError("Enter 10 digits");
                return;
            }
        }

       /* ArrayList<EditText> values = new ArrayList<>();
        values.add(mFirstName);
        values.add(mMiddleName);
        values.add(mLastName);
        values.add(mDOB);
        values.add(mPhoneNum);
        values.add(mAddress1);
        values.add(mAddress2);
        values.add(mCity);
        values.add(mPostal);
        values.add(mRelationship);
        values.add(mOccupation);*/

/*
        if (!mGenderF.isChecked() && !mGenderM.isChecked()) {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
            alertDialogBuilder.setTitle(R.string.error);
            alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_gender);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            return;
        }
*/

        if (!mFirstName.getText().toString().equals("") && !mLastName.getText().toString().equals("")
                && !countryText.getText().toString().equals("") &&
                !state_spinner.getSelectedItem().toString().equals("") && !mAge.getText().toString().equals("") && !mPhoneNum.getText().toString().equals("")
                && (mGenderF.isChecked() || mGenderM.isChecked())) {

            //passes number of days to this function to calculate the actual date...
            if(!mAge.getText().toString().isEmpty() || !mAge.getText().toString().equals("")) {
                String age = getYearFromAge(Integer.parseInt(mAge.getText().toString()));
                birthDate = DateAndTimeUtils.getFormatedDateOfBirth(StringUtils.getValue(age));
                Log.v("age", "birthdate: "+birthDate);
            }
            else {
                //do nothing close the dialog...
            }

            Log.v(TAG, "Result");

        } else {
            if (mFirstName.getText().toString().equals("")) {
                mFirstName.setError(getString(R.string.error_field_required));
            }

            if (mLastName.getText().toString().equals("")) {
                mLastName.setError(getString(R.string.error_field_required));
            }

//            if (mDOB.getText().toString().equals("")) {
//                mDOB.setError(getString(R.string.error_field_required));
//            }

            if (mAge.getText().toString().equals("")) {
                mAge.setError(getString(R.string.error_field_required));
            }

            if (mPhoneNum.getText().toString().equals("")) {
                mPhoneNum.setError(getString(R.string.error_field_required));
            }

           /* if (et_tested_positive_date.getText().toString().equals("")) {
                et_tested_positive_date.setError(getString(R.string.error_field_required));
            }*/

            if (autocompleteState.getText().toString().equals("")) {
                autocompleteState.setError(getString(R.string.error_field_required));
            }

//            if (mCity.getText().toString().equals("")) {
//                mCity.setError(getString(R.string.error_field_required));
//            }

            if (!mGenderF.isChecked() && !mGenderM.isChecked()) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(IdentificationActivity.this);
                alertDialogBuilder.setTitle(R.string.error);
                alertDialogBuilder.setMessage(R.string.identification_screen_dialog_error_gender);
                alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                IntelehealthApplication.setAlertDialogCustomTheme(IdentificationActivity.this, alertDialog);

            }


            Toast.makeText(IdentificationActivity.this, R.string.identification_screen_required_fields, Toast.LENGTH_LONG).show();
            return;
        }

        if (mCountry.getSelectedItemPosition() == 0) {
            countryText.setError(getString(R.string.error_field_required));
            focusView = countryText;
            cancel = true;
            return;
        } else {
            countryText.setError(null);
        }


        /*if (autocompleteState.getText().toString().equalsIgnoreCase("")) {
            autocompleteState.setError(getString(R.string.error_field_required));
            focusView = autocompleteState;
            cancel = true;
            return;
        } else {
            autocompleteState.setError(null);
        }*/

        if (state_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (city_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (block_spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.error_mandatory_field, Toast.LENGTH_SHORT).show();
            return;
        }*/

        /*if (et_tested_positive_date.getText().toString().equals("")) {
            et_tested_positive_date.setError(getString(R.string.error_field_required));
            return;
        } else {
            et_tested_positive_date.setError(null);
        }*/

        // TODO: Add validations for all Spinners here...
/*
        if (occupation_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) occupation_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = occupation_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (occupation_edittext.getVisibility() == View.VISIBLE && occupation_edittext.getText().toString().isEmpty() &&
                occupation_edittext.getText().toString().equalsIgnoreCase("") &&
                occupation_edittext.getText().toString() != null) {

            occupation_edittext.setError(getString(R.string.select));
            focusView = occupation_edittext;
            cancel = true;
            return;
        }
*/


/*
        if (bankaccount_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) bankaccount_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = bankaccount_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (mobilephone_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) mobilephone_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = mobilephone_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (whatsapp_spinner.getSelectedItemPosition() == 0) {
            TextView t = (TextView) whatsapp_spinner.getSelectedView();
            t.setError(getString(R.string.select));
            t.setTextColor(Color.RED);
            focusView = whatsapp_spinner;
            cancel = true;
            return;
        }
*/

/*
        if (familyhead_checkbox.isChecked()) {

            if (no_of_member_edittext.getText().toString().equalsIgnoreCase("") &&
                    no_of_member_edittext.getText().toString().isEmpty()) {
                no_of_member_edittext.setError(getString(R.string.select));
                // no_of_member_edittext.setTextColor(Color.RED);
                focusView = no_of_member_edittext;
                cancel = true;
                return;
            }

            if (no_of_staying_members_edittext.getText().toString().equalsIgnoreCase("") &&
                    no_of_staying_members_edittext.getText().toString().isEmpty()) {
                no_of_staying_members_edittext.setError(getString(R.string.select));
                // no_of_staying_members_edittext.setTextColor(Color.RED);
                focusView = no_of_staying_members_edittext;
                cancel = true;
                return;
            }

            if (source_of_water_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) source_of_water_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = source_of_water_spinner;
                cancel = true;
                return;
            }

            if (howtomake_water_safe_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) howtomake_water_safe_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = howtomake_water_safe_spinner;
                cancel = true;
                return;
            }

            if (watersafe_edittext.getVisibility() == View.VISIBLE && watersafe_edittext.getText().toString().isEmpty() &&
                    watersafe_edittext.getText().toString().equalsIgnoreCase("") &&
                    watersafe_edittext.getText().toString() != null) {

                watersafe_edittext.setError(getString(R.string.select));
                focusView = watersafe_edittext;
                cancel = true;
                return;
            }

            if (!time_water_checkbox.isChecked() && time_water_editText.getText().toString().isEmpty() &&
                    time_water_editText.getText().toString().equalsIgnoreCase("")) {
                //checks if both the fields are not selected...
                time_water_checkbox.setError(getString(R.string.select));

                focusView = time_water_checkbox;
                focusView = time_water_editText;
                cancel = true;
                return;
            }

//            if(time_water_checkbox.isChecked() && time_water_editText.getText().toString().isEmpty() &&
//                    time_water_editText.getText().toString().equalsIgnoreCase("")) {
//                //checks that checkbox is checked but editTExt is empty...
//                time_water_editText.setError("Select");
//                time_water_editText.setTextColor(Color.RED);
//                focusView = time_water_editText;
//                cancel = true;
//                return;
//            }

            if (water_availability_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) water_availability_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = water_availability_spinner;
                cancel = true;
                return;
            }

            if (toilet_facility_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) toilet_facility_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = toilet_facility_spinner;
                cancel = true;
                return;
            }

            if (toiletfacility_edittext.getVisibility() == View.VISIBLE && toiletfacility_edittext.getText().toString().isEmpty() &&
                    toiletfacility_edittext.getText().toString().equalsIgnoreCase("") &&
                    toiletfacility_edittext.getText().toString() != null) {

                toiletfacility_edittext.setError(getString(R.string.select));
                focusView = toiletfacility_edittext;
                cancel = true;
                return;
            }

            if (structure_of_house_spinner.getSelectedItemPosition() == 0) {
                TextView t = (TextView) structure_of_house_spinner.getSelectedView();
                t.setError(getString(R.string.select));
                t.setTextColor(Color.RED);
                focusView = structure_of_house_spinner;
                cancel = true;
                return;
            }

            if (!hectars_land_checkbox.isChecked() && hectars_land_editText.getText().toString().isEmpty() &&
                    hectars_land_editText.getText().toString().equalsIgnoreCase("")) {
                //checks if both the fields are not selected...
                hectars_land_checkbox.setError(getString(R.string.select));

                focusView = hectars_land_checkbox;
                focusView = hectars_land_editText;
                cancel = true;
                return;

            }

//            if(hectars_land_checkbox.isChecked() && hectars_land_editText.getText().toString().isEmpty() &&
//                    hectars_land_editText.getText().toString().equalsIgnoreCase("")) {
//                //checks that checkbox is checked but editTExt is empty...
//                hectars_land_editText.setError("Select");
//                hectars_land_editText.setTextColor(Color.RED);
//                focusView = hectars_land_editText;
//                cancel = true;
//                return;
//            }

        }
*/


        if (cancel) {
            focusView.requestFocus();
        } else {
            if (mCurrentPhotoPath == null)
                mCurrentPhotoPath = patientdto.getPatient_photo();

            patientdto.setFirst_name(StringUtils.getValue(mFirstName.getText().toString()));
            patientdto.setMiddle_name(StringUtils.getValue(mMiddleName.getText().toString()));
            patientdto.setLast_name(StringUtils.getValue(mLastName.getText().toString()));
            patientdto.setPhone_number(StringUtils.getValue(mPhoneNum.getText().toString()));
            patientdto.setGender(StringUtils.getValue(mGender));
            patientdto.setTestedPositiveDate(StringUtils.getValue(et_tested_positive_date.getText().toString()));

            //String dob = StringUtils.hi_or__en(mDOB.getText().toString());
          /*  String[] dob_array = mDOB.getText().toString().split(" ");
            Log.d("dob_array", "0: " + dob_array[0]);
            Log.d("dob_array", "0: " + dob_array[1]);
            Log.d("dob_array", "0: " + dob_array[2]);
*/
            //get month index and return English value for month.
           /* if (dob_indexValue == 15) {
                String dob = StringUtils.hi_or_as__en_noEdit
                        (mDOB.getText().toString(), sessionManager.getAppLanguage());
                patientdto.setDate_of_birth(DateAndTimeUtils.getFormatedDateOfBirth
                        (StringUtils.getValue(dob)));
            } else {
                String dob = StringUtils.hi_or_as__en_month(dob_indexValue);
                String dob_month_split = dob_array[1];
                dob_array[1] = dob_month_split.replace(dob_month_split, dob);
                String dob_value = dob_array[0] + " " + dob_array[1] + " " + dob_array[2];

                patientdto.setDate_of_birth(DateAndTimeUtils.getFormatedDateOfBirth
                        (StringUtils.getValue(dob_value)));
            }*/

            //  patientdto.setDate_of_birth(DateAndTimeUtils.getFormatedDateOfBirth(StringUtils.getValue(dob_value)));
            patientdto.setDate_of_birth(birthDate);
            patientdto.setAddress1(StringUtils.getValue(mAddress1.getText().toString()));
            patientdto.setAddress2(StringUtils.getValue(mAddress2.getText().toString()));
            patientdto.setState_province(StringUtils.getValue(state_spinner.getSelectedItem().toString()));
//            patientdto.setCity_village(StringUtils.getValue(mCity.getText().toString()));
            patientdto.setCity_village(StringUtils.getValue(city_spinner.getSelectedItem().toString()));
            String block = StringUtils.getValue(block_spinner.getSelectedItem().toString());
            patientdto.setEducation_level(block.equalsIgnoreCase("Select") ? "" : block);

            patientdto.setPostal_code(StringUtils.getValue(mPostal.getText().toString()));
            patientdto.setCountry(StringUtils.getValue(mCountry.getSelectedItem().toString()));
            patientdto.setPatient_photo(mCurrentPhotoPath);
//                patientdto.setEconomic(StringUtils.getValue(m));

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Education Level"));
            patientAttributesDTO.setValue(block_spinner.getSelectedItem().toString().equalsIgnoreCase("Select") ? "" : block_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            patientdto.setState_province(StringUtils.getValue(patientdto.getState_province()));
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("caste"));
//            patientAttributesDTO.setValue(StringUtils.getProvided(mCaste));
            patientAttributesDTO.setValue("nurse_app");
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("creatoruuid"));
//            patientAttributesDTO.setValue(StringUtils.getProvided(mCaste));
            patientAttributesDTO.setValue(sessionManager.getProviderID());
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Telephone Number"));
            patientAttributesDTO.setValue(StringUtils.getValue(mPhoneNum.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Son/wife/daughter"));
            patientAttributesDTO.setValue(StringUtils.getValue(mRelationship.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("positiveDate"));
            patientAttributesDTO.setValue(StringUtils.getValue(et_tested_positive_date.getText().toString()));
            patientAttributesDTOList.add(patientAttributesDTO);

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Economic Status"));
            patientAttributesDTO.setValue(StringUtils.getProvided(mEconomicStatus));
            patientAttributesDTOList.add(patientAttributesDTO);

            /*patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Education Level"));
            patientAttributesDTO.setValue(StringUtils.getProvided(mEducation));
            patientAttributesDTOList.add(patientAttributesDTO);*/

            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("ProfileImageTimestamp"));
            patientAttributesDTO.setValue(AppConstants.dateAndTimeUtils.currentDateTime());


            //House Hold Registration
          /*  if (sessionManager.getHouseholdUuid().equals("")) {

                String HouseHold_UUID = UUID.randomUUID().toString();
                sessionManager.setHouseholdUuid(HouseHold_UUID);

                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("householdID"));
                patientAttributesDTO.setValue(HouseHold_UUID);
                patientAttributesDTOList.add(patientAttributesDTO);

            } else {

                String HouseHold_UUID = sessionManager.getHouseholdUuid();
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("householdID"));
                patientAttributesDTO.setValue(HouseHold_UUID);
                patientAttributesDTOList.add(patientAttributesDTO);

            }*/
//          patientAttributesDTOList.add(patientAttributesDTO);
            //Occupation ...
          /*  if (occupation_edittext.getVisibility() == View.VISIBLE && !occupation_edittext.getText().toString().isEmpty() &&
                    !occupation_edittext.getText().toString().equalsIgnoreCase("")) {
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("occupation"));
                patientAttributesDTO.setValue(StringUtils.getValue(occupation_edittext.getText().toString()));
                Log.d("HOH", "Occupation: " + occupation_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);
            } else {
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("occupation"));
                //patientAttributesDTO.setValue(occupation_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(occupation_spinner));
                Log.d("HOH", "Occupation: " + occupation_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);
            }*/

            //Bank Account...
          /*  patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Bank Account"));
            //  patientAttributesDTO.setValue(bankaccount_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(bankaccount_spinner));
            Log.d("HOH", "Bankacc: " + bankaccount_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            //Mobile Phone own...
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Mobile Phone Type"));
            // patientAttributesDTO.setValue(mobilephone_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(mobilephone_spinner));
            Log.d("HOH", "mobile phone type: " + mobilephone_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);

            //WhatsApp Family member ...
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Use WhatsApp"));
            // patientAttributesDTO.setValue(whatsapp_spinner.getSelectedItem().toString());
            patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(whatsapp_spinner));
            Log.d("HOH", "Whatsapp use: " + whatsapp_spinner.getSelectedItem().toString());
            patientAttributesDTOList.add(patientAttributesDTO);
*/
            //Check first if Are you Head of Household checkbox is checked or not...
/*            if (familyhead_checkbox.isChecked()) {
                //Total no of members in household  ...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Total Family Members"));
                patientAttributesDTO.setValue(StringUtils.getValue(no_of_member_edittext.getText().toString()));
                Log.d("HOH", "total family meme: " + no_of_member_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Total no of members living in household  ...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Total Family Members Staying"));
                patientAttributesDTO.setValue(StringUtils.getValue(no_of_staying_members_edittext.getText().toString()));
                Log.d("HOH", "Total family stay: " + no_of_staying_members_edittext.getText().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Main source of drinking water...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Drinking Water Source"));
                // patientAttributesDTO.setValue(source_of_water_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(source_of_water_spinner));
                Log.d("HOH", "source of water: " + source_of_water_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //How do you make Drinking water Safe?
                if (watersafe_edittext.getVisibility() == View.VISIBLE && !watersafe_edittext.getText().toString()
                        .isEmpty() && !watersafe_edittext.getText().toString().equalsIgnoreCase("")) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Safe Drinking Water"));
                    patientAttributesDTO.setValue(StringUtils.getValue(watersafe_edittext.getText().toString()));
                    Log.d("HOH", "water safe: " + watersafe_edittext.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO); //edit text is visible...
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Safe Drinking Water"));
                    // patientAttributesDTO.setValue(howtomake_water_safe_spinner.getSelectedItem().toString());
                    patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(howtomake_water_safe_spinner));
                    Log.d("HOH", "water safe: " + howtomake_water_safe_spinner.getSelectedItem().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                // Time taken to get water...
                if (time_water_checkbox.isChecked()) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Time Drinking Water Source"));
                    // patientAttributesDTO.setValue(StringUtils.getValue(time_water_checkbox.getText().toString()));
                    patientAttributesDTO.setValue(StringUtils.getCheckbox_Hi_En_Hi(time_water_checkbox.getText().toString()));
                    Log.d("HOH", "time to bring water_edit: " + time_water_checkbox.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    //User enters value here...
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Time Drinking Water Source"));
                    patientAttributesDTO.setValue(StringUtils.getValue(time_water_editText.getText().toString()));
                    Log.d("HOH", "time to bring water value entered: " + time_water_editText.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                //            patientAttributesDTO = new PatientAttributesDTO();
//            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
//            patientAttributesDTO.setPatientuuid(uuid);
//            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
//                    .getUuidForAttribute("Time Drinking Water Source"));
//            patientAttributesDTO.setValue(howtomake_water_safe_spinner.getSelectedItem().toString());
//            patientAttributesDTOList.add(patientAttributesDTO);

                //Drinking water availability...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("Drinking Water Availability"));
                // patientAttributesDTO.setValue(water_availability_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(water_availability_spinner));
                Log.d("HOH", "Water availability: " + water_availability_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                //Toilet Facility...
                if (toiletfacility_edittext.getVisibility() == View.VISIBLE && !toiletfacility_edittext
                        .getText().toString().isEmpty() && !toiletfacility_edittext.getText()
                        .toString().equalsIgnoreCase("")) {

                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Toilet Facility"));
                    patientAttributesDTO.setValue(toiletfacility_edittext.getText().toString());
                    Log.d("HOH", "Toilet: " + toiletfacility_edittext.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Toilet Facility"));
                    //  patientAttributesDTO.setValue(toilet_facility_spinner.getSelectedItem().toString());
                    patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(toilet_facility_spinner));
                    Log.d("HOH", "Toilet: " + toilet_facility_spinner.getSelectedItem().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                }

                //House Structure...
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(uuid);
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                        .getUuidForAttribute("House Structure"));
                //  patientAttributesDTO.setValue(structure_of_house_spinner.getSelectedItem().toString());
                patientAttributesDTO.setValue(StringUtils.getSpinnerHi_En(structure_of_house_spinner));
                Log.d("HOH", "Structure: " + structure_of_house_spinner.getSelectedItem().toString());
                patientAttributesDTOList.add(patientAttributesDTO);

                // Hectars of Land...
                if (hectars_land_checkbox.isChecked()) {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Family Cultivable Land"));
                    //  patientAttributesDTO.setValue(StringUtils.getValue(hectars_land_checkbox.getText().toString()));
                    patientAttributesDTO.setValue(StringUtils.getCheckbox_Hi_En_Hi(hectars_land_checkbox.getText().toString()));
                    Log.d("HOH", "Hectars: " + hectars_land_checkbox.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);
                } else {
                    patientAttributesDTO = new PatientAttributesDTO();
                    patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                    patientAttributesDTO.setPatientuuid(uuid);
                    patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO
                            .getUuidForAttribute("Family Cultivable Land"));
                    patientAttributesDTO.setValue(StringUtils.getValue(hectars_land_editText.getText().toString()));
                    Log.d("HOH", "Hectars value entered: " + hectars_land_editText.getText().toString());
                    patientAttributesDTOList.add(patientAttributesDTO);

                }

//                sessionManager.setHOH_checkbox(true);
//                Log.d("session", "session_create: " + sessionManager.getHOH_checkbox());

            }*/
           /* else {
//                sessionManager.setHOH_checkbox(false);
//                Log.d("session", "session_create: " + sessionManager.getHOH_checkbox());
            }*/
            //end of checking if the family head checkbox is checked or not...

            patientAttributesDTOList.add(patientAttributesDTO);
            Logger.logD(TAG, "PatientAttribute list size" + patientAttributesDTOList.size());
            //patientdto.setPatientAttributesDTOList(patientAttributesDTOList);

            Logger.logD("patient json onPatientUpdateClicked : ", "Json : " + gson.toJson(patientdto, Patient.class));

        }
        try {
            Logger.logD(TAG, "update ");
            boolean isPatientUpdated = patientsDAO.updatePatientToDB(patientdto, uuid, patientAttributesDTOList);
            boolean isPatientImageUpdated = imagesDAO.updatePatientProfileImages(mCurrentPhotoPath, uuid);

            if (NetworkConnection.isOnline(getApplication())) {
                SyncDAO syncDAO = new SyncDAO();
                ImagesPushDAO imagesPushDAO = new ImagesPushDAO();
                boolean ispush = syncDAO.pushDataApi();
//                boolean isPushImage = imagesPushDAO.patientProfileImagesPush();

//                if (ispush)
//                    AppConstants.notificationUtils.DownloadDone(getString(R.string.patient_data_upload), "" + patientdto.getFirst_name() + "" + patientdto.getLast_name() + "'s data upload complete.", 2, getApplication());
//                else
//                    AppConstants.notificationUtils.DownloadDone(getString(R.string.patient_data_upload), "" + patientdto.getFirst_name() + "" + patientdto.getLast_name() + "'s data not uploaded.", 2, getApplication());

//                if (isPushImage)
//                    AppConstants.notificationUtils.DownloadDone(getString(R.string.patient_data_upload), "" + patientdto.getFirst_name() + "" + patientdto.getLast_name() + "'s Image upload complete.", 4, getApplication());
//                else
//                    AppConstants.notificationUtils.DownloadDone(getString(R.string.patient_data_upload), "" + patientdto.getFirst_name() + "" + patientdto.getLast_name() + "'s Image not complete.", 4, getApplication());

            }
            if (isPatientUpdated && isPatientImageUpdated) {
                Logger.logD(TAG, "updated");
                Intent i = new Intent(getApplication(), PatientDetailActivity.class);
                i.putExtra("patientUuid", uuid);
                i.putExtra("patientName", patientdto.getFirst_name() + " " + patientdto.getLast_name());
                i.putExtra("tag", "newPatient");
                i.putExtra("hasPrescription", "false");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(i);
            }
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    private void checkUserExistsOrNot(String enteredUserName) {
        cpd.show();
        isUserExists = false;
        checkUserCallExecuted = false;
        UrlModifiers urlModifiers = new UrlModifiers();
        String urlString = urlModifiers.setRegistrationURL();
        String encoded = base64Utils.encoded("admin", BuildConfig.DEBUG ? "IHUser#1" : "IHUser#1");
        Observable<ClsUserGetResponse> userGetResponse = AppConstants.apiInterface.getUsersFromServer(urlString, "Basic " + encoded, enteredUserName);
        userGetResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ClsUserGetResponse>() {
                    @Override
                    public void onNext(ClsUserGetResponse clsUserGetResponse) {
                        checkUserCallExecuted = true;
                        cpd.dismiss();
                        List<ResultsItem> resultList = clsUserGetResponse.getResults();
                        if (resultList != null && resultList.size() > 0) {
                            isUserExists = true;
                            /*isUSerExistsAlready = false;
                            image_username_valid.setVisibility(View.VISIBLE);*/
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IdentificationActivity.this);
                            alertDialogBuilder.setMessage(R.string.warning_user_exist);
                            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //if number exists redirect the patient the search activity(prefilled with mobile number in the search bar)
                                    SearchPatientActivity.start(IdentificationActivity.this, enteredUserName);
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        } else {
                            isUserExists = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        cpd.dismiss();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void createUserMapping(String personUUID, String enteredUserName, String password) {
        cpd.show();
        UrlModifiers urlModifiers = new UrlModifiers();
        String urlString = urlModifiers.getUserMapping(sessionManager.getServerUrl());
        String encoded = base64Utils.encoded("admin", BuildConfig.DEBUG ? "IHUser#1" : "IHUser#1");
        GetPassword getPassword = new GetPassword();
        getPassword.username = enteredUserName;
        getPassword.password = password;
        Observable<GetPassword> userGetResponse = AppConstants.apiInterface.getUserMapping(urlString, "Basic " + encoded, getPassword);
        userGetResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<GetPassword>() {
                    @Override
                    public void onNext(GetPassword response) {
                        cpd.dismiss();
                        onPatientCreateClicked(personUUID);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cpd.dismiss();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private String getYearFromAge(int dateString) {
        String date = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",
                Locale.ENGLISH);

        //number of days before date...
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -dateString);
        date = simpleDateFormat.format(calendar.getTime());
        Log.v("time", "todays date: " + date);
        //number of days calculation...

        return date;
    }


    void createMedicalAdviceVisit() {
        //formats used in databases to store the start & end date
        SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
//        SimpleDateFormat endFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH);
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MINUTE, -1);
        today.set(Calendar.MILLISECOND, 0);
        Date todayDate = today.getTime();
        String endDate = startFormat.format(todayDate);
        today.add(Calendar.MILLISECOND, (int) -TimeUnit.MINUTES.toMillis(5));
        String startDate = startFormat.format(today.getTime());

        //create & save visit visitUuid & encounter in the DB
        String visitUuid = UUID.randomUUID().toString();
        EncounterDAO encounterDAO = new EncounterDAO();
        EncounterDTO encounterDTO = new EncounterDTO();
        encounterDTO.setUuid(UUID.randomUUID().toString());
        encounterDTO.setEncounterTypeUuid(UuidDictionary.ENCOUNTER_ADULTINITIAL);
        encounterDTO.setEncounterTime(startDate);
        encounterDTO.setVisituuid(visitUuid);
        encounterDTO.setSyncd(false);
        encounterDTO.setProvideruuid(sessionManager.getProviderID());
        Log.d("DTO", "DTO:detail " + encounterDTO.getProvideruuid());
        encounterDTO.setVoided(0);
        encounterDTO.setPrivacynotice_value(getString(R.string.accept));//privacy value added.

        try {
            encounterDAO.createEncountersToDB(encounterDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        boolean returning = false;
        sessionManager.setReturning(returning);

        //create & save visit in the DB
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setUuid(visitUuid);
        visitDTO.setPatientuuid(this.uuid);
        visitDTO.setStartdate(startDate);
//        visitDTO.setEnddate(endDate);
        visitDTO.setVisitTypeUuid(UuidDictionary.VISIT_TELEMEDICINE);
        visitDTO.setLocationuuid(sessionManager.getLocationUuid());
        visitDTO.setSyncd(false);
        visitDTO.setCreatoruuid(sessionManager.getCreatorID());//static
        VisitsDAO visitsDAO = new VisitsDAO();

        try {
            visitsDAO.insertPatientToDB(visitDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        //create & save obs data in the DB
        ObsDAO obsDAO = new ObsDAO();
        ObsDTO obsDTO = new ObsDTO();
        obsDTO.setConceptuuid(UuidDictionary.CURRENT_COMPLAINT);
        obsDTO.setEncounteruuid(encounterDTO.getUuid());
        obsDTO.setCreator(sessionManager.getCreatorID());

        //append all the selected items to the OBS value
        String insertion = Node.bullet_arrow + "<b>" + "Medical Advice" + "</b>" + ": ";
        if (cbVaccineGuide.isChecked())
            insertion = insertion.concat(Node.next_line + cbVaccineGuide.getText());
        if (cbCovidConcern.isChecked())
            insertion = insertion.concat(Node.next_line + cbCovidConcern.getText());
        if (cbManagingBreathlessness.isChecked())
            insertion = insertion.concat(Node.next_line + cbManagingBreathlessness.getText());
        if (cbManageVoiceIssue.isChecked())
            insertion = insertion.concat(Node.next_line + cbManageVoiceIssue.getText());
        if (cbManageEating.isChecked())
            insertion = insertion.concat(Node.next_line + cbManageEating.getText());
        if (cbDealProblems.isChecked())
            insertion = insertion.concat(Node.next_line + cbDealProblems.getText());
        if (cbMentalHealth.isChecked())
            insertion = insertion.concat(Node.next_line + cbMentalHealth.getText());
        if (cbExercises.isChecked())
            insertion = insertion.concat(Node.next_line + cbExercises.getText());
        if (cbOthers.isChecked())
            insertion = insertion.concat(Node.next_line + String.format("%s: %s", cbOthers.getText(), et_medical_advice_extra.getText()));
        if (!TextUtils.isEmpty(et_medical_advice_additional.getText()))
            insertion = insertion.concat(Node.next_line + String.format("%s: %s", getString(R.string.txt_additional_info), et_medical_advice_additional.getText()));
        obsDTO.setValue(insertion);

        obsDTO.setUuid(AppConstants.NEW_UUID);

        try {
            obsDAO.insertObs(obsDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        //create & save visit attributes - required for syncing the data
        VisitAttributeListDAO speciality_attributes = new VisitAttributeListDAO();
        try {
            speciality_attributes.insertVisitAttributes(visitUuid, AppConstants.SPECIALIST_DOCTOR_NOT_NEEDED);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        endVisit(visitUuid, uuid, endDate);
    }

    private void endVisit(String visitUuid, String patientUuid, String endTime) {
        VisitsDAO visitsDAO = new VisitsDAO();
        try {
            visitsDAO.updateVisitEnddate(visitUuid, endTime);
            Toast.makeText(this, R.string.text_patient_and_advice_created, Toast.LENGTH_SHORT).show();
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        new SyncUtils().syncForeground(""); //Sync function will work in foreground of app and
        sessionManager.removeVisitSummary(patientUuid, visitUuid);
    }
}
