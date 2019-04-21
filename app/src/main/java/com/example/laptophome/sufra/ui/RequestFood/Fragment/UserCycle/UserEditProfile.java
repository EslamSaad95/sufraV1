package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.usercycle.userprofile.UserProfile;
import com.example.laptophome.sufra.data.model.usercycle.userregister.UserRegister;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.HelperWebServicesMethods;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserEditProfile extends Fragment {
    @BindView(R.id.EditInfoFr_PicIv)
    ImageView EditInfoFrPicIv;
    @BindView(R.id.EditInfoFr_AddPicTv)
    TextView EditInfoFrAddPicTv;
    @BindView(R.id.EditInfoFr_PicLayout)
    LinearLayout EditInfoFrPicLayout;
    @BindView(R.id.EditInfoFr_NameEt)
    TextInputLayout EditInfoFrNameEt;
    @BindView(R.id.EditInfoFr_EmailEt)
    TextInputLayout EditInfoFrEmailEt;
    @BindView(R.id.EditInfoFr_PhoneEt)
    TextInputLayout EditInfoFrPhoneEt;
    @BindView(R.id.EditInfoFr_CitySp)
    Spinner EditInfoFrCitySp;
    @BindView(R.id.EditInfoFr_CitySp_Layout)
    RelativeLayout EditInfoFrCitySpLayout;
    @BindView(R.id.EditInfoFr_RegionSp)
    Spinner EditInfoFrRegionSp;
    @BindView(R.id.EditInfoFr_RegionSp_Layout)
    RelativeLayout EditInfoFrRegionSpLayout;
    @BindView(R.id.EditInfoFr_HomeAddressDescrEt)
    TextInputLayout EditInfoFrHomeAddressDescrEt;
    @BindView(R.id.EditInfoFr_PasswordEt)
    TextInputLayout EditInfoFrPasswordEt;
    @BindView(R.id.EditInfoFr_RepeatPassEt)
    TextInputLayout EditInfoFrRepeatPassEt;
    @BindView(R.id.EditInfoFr_EditBtn)
    Button EditInfoFrEditBtn;
    Unbinder unbinder;
    private ApiService apiService;
    String Api_Token,User_Password,User_Address,User_Name,User_Email,User_Phone;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.edit_yrinformation, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Api_Token,getContext());
        User_Password=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Password,getContext());
        User_Name=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Name_Key,getContext());
        User_Address=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Address_Key,getContext());
        User_Email=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.UserEmail,getContext());
        User_Phone=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Phone_Key,getContext());

        HelperMethod.SetToolBar(getString(R.string.EditProfile),true,getContext());

        HelperWebServicesMethods.GetCities(getContext(), EditInfoFrCitySp);
      HelperWebServicesMethods.GetRegions(getContext(),EditInfoFrRegionSp,EditInfoFrCitySp);


        LoadInformation();
        return view;
    }

    public void LoadInformation() {


        EditInfoFrNameEt.getEditText().setText(User_Name);
        EditInfoFrEmailEt.getEditText().setText(User_Email);
        EditInfoFrPhoneEt.getEditText().setText(User_Phone);
        EditInfoFrPasswordEt.getEditText().setText(User_Password);
        EditInfoFrRepeatPassEt.getEditText().setText(User_Password);
        EditInfoFrHomeAddressDescrEt.getEditText().setText(User_Address);
        EditInfoFrCitySp.setSelection(2);
        EditInfoFrCitySp.getItemAtPosition(3);
    }
    public void EditClientInformation()
    {progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
            apiService.EditUserProfile(Api_Token,EditInfoFrNameEt.getEditText().getText().toString(),
                    EditInfoFrPhoneEt.getEditText().getText().toString(),EditInfoFrEmailEt.getEditText().getText().toString()
            , EditInfoFrPasswordEt.getEditText().getText().toString(),EditInfoFrRepeatPassEt.getEditText().getText().toString(),
                    EditInfoFrHomeAddressDescrEt.getEditText().getText().toString(),EditInfoFrRegionSp.getId()).enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    progressDialog.dismiss();
                        try {

                            if(response.body().getStatus().equals(1))
                            {
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Name_Key,EditInfoFrNameEt.getEditText().getText().toString(),getContext());
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.UserEmail,EditInfoFrEmailEt.getEditText().getText().toString(),getContext());
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Phone_Key,EditInfoFrPhoneEt.getEditText().getText().toString(),getContext());
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Password,EditInfoFrPasswordEt.getEditText().getText().toString(),getContext());
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Address_Key,EditInfoFrHomeAddressDescrEt.getEditText().getText().toString(),getContext());

                            }
                            else
                            {
                                Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception ee)
                        {}
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.EditInfoFr_PicIv, R.id.EditInfoFr_EditBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.EditInfoFr_PicIv:
                break;
            case R.id.EditInfoFr_EditBtn:
                EditClientInformation();
                break;
        }
    }
}
