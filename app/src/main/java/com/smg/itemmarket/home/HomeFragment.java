package com.smg.itemmarket.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smg.itemmarket.DBKey;
import com.smg.itemmarket.R;
import com.smg.itemmarket.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding = null;
    private ArticleAdapter articleAdapter;
    private ArrayList<ArticleModel> articleModels = new ArrayList<ArticleModel>();

    private FirebaseAuth auto;
    private DatabaseReference articleDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentHomeBinding fragmentHomeBinding = FragmentHomeBinding.bind(view);
        binding = fragmentHomeBinding;

        auto = FirebaseAuth.getInstance();
        articleDB = FirebaseDatabase.getInstance().getReference(DBKey.DB_ARTICLES);

        articleModels.clear();
        articleAdapter = new ArticleAdapter(articleModels);

        fragmentHomeBinding.articleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //fragmentHomeBinding.articleRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)); // 구분선 넣어주는 옵션
        fragmentHomeBinding.articleRecyclerView.setAdapter(articleAdapter);

        fragmentHomeBinding.addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo
                /*
                if(auto.getCurrentUser() != null) {
                    startActivity(new Intent(requireContext(), AddArticleActivity.class));
                } else {
                    Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show();
                }
                */
                startActivity(new Intent(requireContext(), AddArticleActivity.class));
            }
        });

        articleDB.addChildEventListener(childEventListener);



        /*
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto.signInWithEmailAndPassword(mUserId.getText().toString(), mPassWord.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("smg", "로그인 성공");
                                } else {
                                    Log.d("smg", "로그인 실패");
                                }
                            }
                        });
            }
        });

        mAssing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto.createUserWithEmailAndPassword(mUserId.getText().toString(), mPassWord.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("smg", "회원가입 성공");
                                } else {
                                    Log.d("smg", "회원가입 실패");
                                }
                            }
                        });
            }
        });
        */
    }

    @Override
    public void onResume() {
        super.onResume();

        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        articleDB.removeEventListener(childEventListener);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            ArticleModel articleModel = snapshot.getValue(ArticleModel.class);

            if(articleModel == null) return;
            articleModels.add(articleModel);
            articleAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };

    // 테스트
    private void load() {
        articleModels.add(new ArticleModel("0", "test" ,10000L, "100", ""));
        articleAdapter.notifyDataSetChanged();
    }
}
