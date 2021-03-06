package com.example.sashok.jsoupparserapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.jsoupparserapp.MainActivity;
import com.example.sashok.jsoupparserapp.R;
import com.example.sashok.jsoupparserapp.adapter.FolderListAdapter;
import com.example.sashok.jsoupparserapp.interfaces.ShowFoldersInterface;
import com.example.sashok.jsoupparserapp.model.Folder;
import com.example.sashok.jsoupparserapp.util.AssetFolderUtil;
import com.example.sashok.jsoupparserapp.util.FileFolderUtil;
import com.example.sashok.jsoupparserapp.util.FolderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 2.10.17.
 */

public class ShowFoldersFragment extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener, ShowFoldersInterface {
    private RecyclerView rvFolders;
    private FolderListAdapter mAdapter;
    private List<Folder> folders;
    private int cur_folder;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folders = new ArrayList<>();
    }

    public void initViews(View root_view) {
        rvFolders = (RecyclerView) root_view.findViewById(R.id.folder_recycler_view);
        rvFolders.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        rvFolders.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) root_view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_folders_layout, container, false);
        initViews(view);
        getFolders();
        return view;
    }

    @Override
    public void getFolders() {
        swipeRefreshLayout.setRefreshing(true);
        List<Folder> saved_folders = FolderUtil.fetchFolders(new AssetFolderUtil(getActivity()));
        if (saved_folders == null) {
            saved_folders = FolderUtil.fetchFolders(new FileFolderUtil(getActivity()));
        } else {
            folders = saved_folders;
            onFoldersFetches();
        }
    }

    @Override
    public void onFolderClicked(Folder folder) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getFolders();
    }


    @Override
    public void onFoldersFetches() {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter = new FolderListAdapter(getActivity(), this.folders, (MainActivity) getActivity());
        rvFolders.setAdapter(mAdapter);

    }

    public static ShowFoldersFragment newInstance(Bundle args) {
        ShowFoldersFragment fragment = new ShowFoldersFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
