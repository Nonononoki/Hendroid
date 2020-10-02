package me.devsaki.hentoid.fragments.library;

import androidx.fragment.app.DialogFragment;

/**
 * Created by Robb on 11/2018
 * Launcher dialog for the library refresh feature
 */
public class UpdateSuccessDialogFragment extends DialogFragment {

    /*
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ItemAdapter<GitHubReleaseItem> itemAdapter = new ItemAdapter<>();

    public static void invoke(FragmentManager fragmentManager) {
        UpdateSuccessDialogFragment fragment = new UpdateSuccessDialogFragment();
        fragment.show(fragmentManager, "usdf");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View rootView = inflater.inflate(R.layout.dialog_library_update_success, container, false);

        FastAdapter<GitHubReleaseItem> releaseItemAdapter = FastAdapter.with(itemAdapter);
        RecyclerView releaseItem = requireViewById(rootView, R.id.changelogReleaseItem);
        releaseItem.setAdapter(releaseItemAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getReleases();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void getReleases() {
        compositeDisposable.add(GithubServer.API.getLatestRelease()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckSuccess, this::onCheckError)
        );
    }

    private void onCheckSuccess(GitHubReleaseItem.Struct latestReleaseInfo) {
        itemAdapter.add(new GitHubReleaseItem(latestReleaseInfo));
    }

    private void onCheckError(Throwable t) {
        Timber.w(t, "Error fetching GitHub latest release data");
    }
     */
}
