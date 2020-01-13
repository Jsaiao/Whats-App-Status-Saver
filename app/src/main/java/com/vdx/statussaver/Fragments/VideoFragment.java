package com.vdx.statussaver.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
import com.vdx.statussaver.Activities.VideoShowActivity;
import com.vdx.statussaver.Adapters.VideoStatusAdpater;
import com.vdx.statussaver.Models.VideoType;
import com.vdx.statussaver.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static com.vdx.statussaver.Activities.DrawerActivity.toolbar;


public class VideoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<VideoType> videoList;
    private PullRefreshLayout pullRefreshLayout;
    private LinearLayout textContainer;
    private VideoStatusAdpater videoStatusAdpater;
    private Button refresh;


    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;


    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_fragmeny, container, false);
        recyclerView = view.findViewById(R.id.video_list);
        refresh = view.findViewById(R.id.ref_btn_vid);
        pullRefreshLayout = view.findViewById(R.id.v_refresh);
        pullRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.c1),
                getResources().getColor(R.color.c2),
                getResources().getColor(R.color.c3),
                getResources().getColor(R.color.c4));

//        pullRefreshLayout.setRefreshDrawable(new RefreshDrawable(getActivity(), pullRefreshLayout));
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        textContainer = view.findViewById(R.id.v_container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFolderList();
        pullRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        textContainer.setVisibility(View.GONE);

        if (videoList.size() == 0) {
            pullRefreshLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            textContainer.setVisibility(View.VISIBLE);
        }

        videoStatusAdpater = new VideoStatusAdpater(videoList, getContext());

        videoStatusAdpater.setVideoClickListener(new VideoStatusAdpater.onVideoClickListener() {
            @Override
            public void onItemClick(View view, VideoType obj, int pos) {
                if (videoStatusAdpater.getSelectedVideoItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    Intent intent = new Intent(getContext(), VideoShowActivity.class);
                    intent.putExtra("videoPath", obj.getVideoFile());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, VideoType obj, int pos) {
                enableActionMode(pos);
            }
        });

        if (videoList.size() == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        recyclerView.setAdapter(videoStatusAdpater);

//        if (!isempty) {
//            pullRefreshLayout.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//            textContainer.setVisibility(View.GONE);
//
//            videoStatusAdpater = new VideoStatusAdpater(videoList, getContext());
//            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//            recyclerView.setAdapter(videoStatusAdpater);
//        } else {
//            textContainer.setVisibility(View.VISIBLE);
//        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        });
        refresh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        actionModeCallback = new ActionModeCallback();

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getFolderList() {

        videoList = new ArrayList<>();

        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses");
        Log.d("file", String.valueOf(fileDirectory));
        File[] dirFiles = fileDirectory.listFiles();
        try {
            assert dirFiles != null;
            if (dirFiles.length != 0) {
                // loops through the array of files, outputing the name to console
                for (int ii = 0; ii < dirFiles.length; ii++) {
                    String fileOutput = dirFiles[ii].toString();
                    System.out.println(fileOutput);
                    if (fileOutput.contains(".mp4")) {
                        videoList.add(new VideoType(fileOutput));
                    }
                }

            }
        } catch (Exception e) {
            Log.e("error", String.valueOf(e));
        }
    }

    private void Refresh() {
        getFolderList();
        if (videoList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            pullRefreshLayout.setVisibility(View.VISIBLE);
            textContainer.setVisibility(View.GONE);
            videoStatusAdpater = new VideoStatusAdpater(videoList, getContext());

            videoStatusAdpater.setVideoClickListener(new VideoStatusAdpater.onVideoClickListener() {
                @Override
                public void onItemClick(View view, VideoType obj, int pos) {
                    if (videoStatusAdpater.getSelectedVideoItemCount() > 0) {
                        enableActionMode(pos);
                    } else {
                        Intent intent = new Intent(getContext(), VideoShowActivity.class);
                        intent.putExtra("videoPath", obj.getVideoFile());
                        startActivity(intent);
                    }
                }

                @Override
                public void onItemLongClick(View view, VideoType obj, int pos) {
                    enableActionMode(pos);
                }
            });


            if (videoList.size() == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(videoStatusAdpater);
            ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
            scaleInAnimationAdapter.setDuration(300);
            recyclerView.setAdapter(scaleInAnimationAdapter);
            recyclerView.invalidate();
            videoStatusAdpater.notifyDataSetChanged();
            pullRefreshLayout.setRefreshing(false);
        } else {
            recyclerView.setVisibility(View.GONE);
            pullRefreshLayout.setVisibility(View.GONE);
            textContainer.setVisibility(View.VISIBLE);
            pullRefreshLayout.setRefreshing(false);
        }

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = Objects.requireNonNull(getActivity()).startActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        videoStatusAdpater.toggleVideoSelection(position);
        int count = videoStatusAdpater.getSelectedVideoItemCount();

        if (count == 0) {
            toolbar.setVisibility(View.VISIBLE);
            actionMode.finish();
        } else {
            toolbar.setVisibility(View.GONE);
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
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
                mode.finish();
                return true;
            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            videoStatusAdpater.clearVideoSelections();
            actionMode = null;
        }
    }


    private void deleteVideo() {
        List<Integer> selectedItemPositions = videoStatusAdpater.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            videoStatusAdpater.removeVideoData(selectedItemPositions.get(i));
            System.out.println(selectedItemPositions.get(i));
        }
        videoStatusAdpater.notifyDataSetChanged();
    }

    private void SaveFile() {
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        List<Integer> selectedItemPositions = videoStatusAdpater.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            videoStatusAdpater.saveVideoData(selectedItemPositions.get(i));
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
                mode.finish();
                dialog.dismiss();

            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideo();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                mode.finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
