package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.RecordingWrapper;
import admin4.techelm.com.techelmtechnologies.utility.PlaybackFragment;

public class ServiceJobRecordingsListAdapter extends RecyclerView.Adapter<ServiceJobRecordingsListAdapter.ViewHolder> {

    private static final String LOG_TAG = "RecordingsListAdapter";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<RecordingWrapper> mDataSet = new ArrayList<>();
    private RecordingWrapper serviceJobDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public ServiceJobRecordingsListAdapter(Activity context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the CallbackInterface in the Activity", ex);
        }
        // System.gc();
    }

    public ServiceJobRecordingsListAdapter(List<RecordingWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<RecordingWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_voice_record_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewNameRecord.setText(serviceJobDataSet.getName() + " uploaded.");
        // holder.textViewDeleteRecord.setText(serviceJobDataSet.getServiceNumber());
        // holder.imageButtonDelete.setOnClickListener(this);

        // define an on click listener to open PlaybackFragment
        /*holder.imageButtonUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlaybackFragment playbackFragment =
                            new PlaybackFragment().newInstance(serviceJobDataSet);

                    FragmentTransaction transaction = ((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                            .beginTransaction();

                    playbackFragment.show(transaction, "dialog_playback");

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                }
            }
        });*/

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                serviceJobDataSet.getFilePath() + serviceJobDataSet.getName());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void animateItem(View view) {
        view.setTranslationY(getScreenHeight());
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position - the position
         * @param recordingWrapper - the text to pass back
         */
        void onHandleRecordingsSelection(int position, RecordingWrapper recordingWrapper, int mode);
        void onHandleDeleteRecordingsFromListSelection(int id);
        void onHandlePlayFromListSelection(RecordingWrapper recordingWrapper);
    }

    public interface OnItemClickListener {
        void onClick(RecordingWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNameRecord;
        TextView textViewDeleteRecord;

        ImageButton imageButtonDelete;
        ImageButton imageButtonUploaded;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewNameRecord = (TextView) view.findViewById(R.id.textViewNameRecord);
            textViewDeleteRecord = (TextView) view.findViewById(R.id.textViewDeleteRecord);

            // ImageButtons
            imageButtonDelete = (ImageButton) view.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setOnClickListener(this);

            imageButtonUploaded = (ImageButton) view.findViewById(R.id.imageButtonUploaded);
            imageButtonUploaded.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*if (v.getId() == buttonSpeakAlphabet.getId()) {
                if (mCallback != null){
                    mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 1);
                    //Toast.makeText(v.getContext(), "TEST: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            } else*/

            if (v.getId() == imageButtonDelete.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeleteRecordingsFromListSelection(mDataSet.get(getAdapterPosition()).getId());
                }
            } else if (v.getId() == imageButtonUploaded.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandlePlayFromListSelection(mDataSet.get(getAdapterPosition()));
                }

            }

        }
    }
}
