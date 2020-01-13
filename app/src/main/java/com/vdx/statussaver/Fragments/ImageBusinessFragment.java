package com.vdx.statussaver.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.vdx.statussaver.Activities.ImageShowActivity;
import com.vdx.statussaver.Adapters.ImageStatusAdpater;
import com.vdx.statussaver.Models.ImageType;
import com.vdx.statussaver.R;
import com.vdx.statussaver.Utils.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static com.vdx.statussaver.Utils.TotalList.imgBusinessList;
import static com.vdx.statussaver.Utils.TotalList.imgList;


public class ImageBusinessFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ImageType> imageList;

    private PullRefreshLayout pullRefreshLayout;
    private LinearLayout textContainer;
    private ImageStatusAdpater imageStatusAdpater;
    private Button refresh;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Helper helper;

    public ImageBusinessFragment() {
        // Required empty public constructor
    }

    public static ImageBusinessFragment newInstance() {
        return new ImageBusinessFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        recyclerView = view.findViewById(R.id.status_list);
        refresh = view.findViewById(R.id.ref_bth_img);
        pullRefreshLayout = view.findViewById(R.id.i_refresh);
        pullRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.c1),
                getResources().getColor(R.color.c2),
                getResources().getColor(R.color.c3),
                getResources().getColor(R.color.c4));

//        pullRefreshLayout.setRefreshDrawable(new RefreshDrawable(getActivity(), pullRefreshLayout));
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        textContainer = view.findViewById(R.id.container_image);
        helper = new Helper();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFolderList();

        pullRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        textContainer.setVisibility(View.GONE);

        if (imageList.size() == 0) {
            textContainer.setVisibility(View.VISIBLE);
            pullRefreshLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        imageStatusAdpater = new ImageStatusAdpater(imageList, getContext());
        imageStatusAdpater.setOnClickListener(new ImageStatusAdpater.OnClickListener() {
            @Override
            public void onItemClick(View view, ImageType obj, int pos) {
                if (imageStatusAdpater.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    Intent intent = new Intent(getContext(), ImageShowActivity.class);
                    intent.putExtra("pos", pos);
                    startActivity(intent);

                }
            }

            @Override
            public void onItemLongClick(View view, ImageType obj, int pos) {
                enableActionMode(pos);
            }
        });

        if (imageList.size() == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        }
        recyclerView.setAdapter(imageStatusAdpater);
        recyclerView.setHasFixedSize(true);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        });
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });

        actionModeCallback = new ActionModeCallback();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = Objects.requireNonNull(getActivity()).startActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        imageStatusAdpater.toggleSelection(position);
        int count = imageStatusAdpater.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }


    private void getFolderList() {
        imageList = new ArrayList<>();
        imgBusinessList.clear();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/.Statuses");
        Log.d("file", String.valueOf(fileDirectory));
        File[] dirFiles = fileDirectory.listFiles();
        try {
            assert dirFiles != null;
            if (dirFiles.length != 0) {
                // loops through the array of files, outputing the name to console
                for (File dirFile : dirFiles) {
                    String fileOutput = dirFile.toString();
                    System.out.println(fileOutput);
                    if (fileOutput.contains(".jpg")) {
                        imageList.add(new ImageType(fileOutput));
                        imgBusinessList.add(new ImageType(fileOutput));
                    }
                }

            }
        } catch (Exception e) {
            Log.e("error", String.valueOf(e));
        }
    }

    private void Refresh() {
        getFolderList();
        if (imageList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            pullRefreshLayout.setVisibility(View.VISIBLE);
            textContainer.setVisibility(View.GONE);

            imageStatusAdpater = new ImageStatusAdpater(imageList, getContext());
            if (imageList.size() == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
            imageStatusAdpater.setOnClickListener(new ImageStatusAdpater.OnClickListener() {
                @Override
                public void onItemClick(View view, ImageType obj, int pos) {
                    if (imageStatusAdpater.getSelectedItemCount() > 0) {
                        enableActionMode(pos);
                    } else {
                        Intent intent = new Intent(getContext(), ImageShowActivity.class);
                        intent.putExtra("pos", pos);
                        startActivity(intent);
                    }
                }

                @Override
                public void onItemLongClick(View view, ImageType obj, int pos) {
                    enableActionMode(pos);
                }
            });

            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(imageStatusAdpater);
            ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
            scaleInAnimationAdapter.setDuration(300);
            recyclerView.setAdapter(scaleInAnimationAdapter);
            recyclerView.invalidate();
            recyclerView.setHasFixedSize(true);

            imageStatusAdpater.notifyDataSetChanged();
            pullRefreshLayout.setRefreshing(false);
        } else {
            recyclerView.setVisibility(View.GONE);
            pullRefreshLayout.setVisibility(View.GONE);
            textContainer.setVisibility(View.VISIBLE);
            pullRefreshLayout.setRefreshing(false);
        }

    }


    @Override
    public void onResume() {

        super.onResume();
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            Tools.setSystemBarColor(MultiSelect.this, R.color.colorDarkBlue2);
            mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                dialogDelete(mode);

                return true;
            } else if (id == R.id.action_save) {
                SaveFile();
                actionMode.finish();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            imageStatusAdpater.clearSelections();
            actionMode = null;
        }
    }


    private void deleteImage() {
        List<Integer> selectedItemPositions = imageStatusAdpater.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            imageStatusAdpater.removeData(selectedItemPositions.get(i));
        }
        imageStatusAdpater.notifyDataSetChanged();
    }

    private void SaveFile() {
        List<Integer> selectedItemPositions = imageStatusAdpater.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            imageStatusAdpater.saveData(selectedItemPositions.get(i));
        }
    }


    private void dialogDelete(final ActionMode mode) {

        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.delete_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel = dialog.findViewById(R.id.cancel);
        Button delete = dialog.findViewById(R.id.delete);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                mode.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
