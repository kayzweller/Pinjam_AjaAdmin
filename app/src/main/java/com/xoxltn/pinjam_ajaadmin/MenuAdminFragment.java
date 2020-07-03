package com.xoxltn.pinjam_ajaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MenuAdminFragment extends Fragment {

    private View mView;
    private TextView mTextName, mTextEmail;

    private FirebaseAuth mAuth;
    private FirebaseUser mFireUser;
    private DocumentReference mDocRef;

    private String mName, mEmail;

    //-------------------------------------------------------------------------------------------//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_menu_admin, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFireUser = mAuth.getCurrentUser();

        if (mFireUser != null) {
            String userID = mFireUser.getUid();
            FirebaseFirestore fire = FirebaseFirestore.getInstance();
            mDocRef = fire.collection("USER").document(userID);
        }

        mTextName = mView.findViewById(R.id.text_admin);
        mTextEmail = mView.findViewById(R.id.text_admin_email);

        callDataAdmin();
        callButtonLogout();

        return mView;
    }

    //-------------------------------------------------------------------------------------------//

    private void callDataAdmin() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot docLog = Objects.requireNonNull(task.getResult());
                mName = docLog.getString("name");
                mEmail = mFireUser.getEmail();
                setText(mName, mEmail);
            }
        });
    }

    private void setText(String name, String email) {
        mTextName.setText(name);
        mTextEmail.setText(email);
    }

    //-------------------------------------------------------------------------------------------//

    private void callButtonLogout() {
        Button logout = mView.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();

                    Toast.makeText(getActivity(), "LOGOUT BERHASIL", Toast.LENGTH_SHORT)
                            .show();

                    Intent backToLogin = new Intent(getActivity(), MainLoginActivity.class);
                    backToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    backToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(backToLogin);
                    Objects.requireNonNull(getActivity()).finish();
                }

            }
        });
    }

}
