package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class UnsignedServiceJobListAdapter extends RecyclerView.Adapter<UnsignedServiceJobListAdapter.ViewHolder> {

    private static final String LOG_TAG = "RecyclerViewAdapter";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ServiceJobWrapper> mDataSet = new ArrayList<>();
    private ServiceJobWrapper serviceJobDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public UnsignedServiceJobListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
        System.gc();
    }

    public UnsignedServiceJobListAdapter(List<ServiceJobWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ServiceJobWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_number_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(serviceJobDataSet.getServiceNumber());
        holder.textViewDateNumber.setText(serviceJobDataSet.getDay());
        holder.textViewDate.setText(serviceJobDataSet.getDate());
        holder.textViewServiceNum.setText(serviceJobDataSet.getServiceNumber());
        holder.textViewCustomer.setText(serviceJobDataSet.getCustomer());
        holder.textViewEngineer.setText(serviceJobDataSet.getEngineer());
        holder.textViewStatus.setText(serviceJobDataSet.getStatus());
        holder.textViewStatus.setTextColor((serviceJobDataSet.getID()%2 == 1) ? Color.RED : Color.BLUE);

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " + serviceJobDataSet.getServiceNumber());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }

        // Use your listener to pass back the data used FOR callback
        // Can be Implemented here or at the ViewHolder
        /*holder.buttonSpeakAlphabet.onClick(new View.onClick() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    mCallback.onHandleRecordingsSelection(position, mDataSet.get(position));
                    Toast.makeText(v.getContext(), "Item " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static int getScreenHeight() {
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
         * @param servicejob     - the text to pass back
         */
        void onHandleSelection(int position, ServiceJobWrapper servicejob, int mode);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textViewDateNumber;
        public final TextView textViewDay;
        public final TextView textViewDate;

        public final TextView textViewServiceNum;
        public final TextView textViewCustomer;
        public final TextView textViewEngineer;
        public final TextView textViewStatus;

        public final ImageButton buttonEditDetails;
        public final ImageButton buttonViewDetails;
        public final TextView textViewEditDetails;
        public final TextView textViewViewDetails;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewDateNumber = (TextView) view.findViewById(R.id.textViewDateNumber);
            textViewDay = (TextView) view.findViewById(R.id.textViewDay);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);

            // Data from DB
            textViewServiceNum = (TextView) view.findViewById(R.id.textViewServiceNum);
            textViewCustomer = (TextView) view.findViewById(R.id.textViewCustomer);
            textViewEngineer = (TextView) view.findViewById(R.id.textViewEngineer);
            textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

            // ImageButtons
            buttonEditDetails = (ImageButton) view.findViewById(R.id.buttonEditDetails);
            buttonEditDetails.setOnClickListener(this);
            buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
            buttonViewDetails.setOnClickListener(this);

            // ImageButton Links
            textViewEditDetails = (TextView) view.findViewById(R.id.textViewEditDetails);
            //textViewEditDetails.setOnClickListener(this);
            textViewEditDetails.setText(Html.fromHtml("<u>Edit</u>"));
            textViewViewDetails = (TextView) view.findViewById(R.id.textViewViewDetails);
            //textViewViewDetails.setOnClickListener(this);
            textViewViewDetails.setText(Html.fromHtml("<u>View</u>"));
        }

        @Override
        public void onClick(View v) {
            /*if (v.getId() == buttonSpeakAlphabet.getId()) {
                if (mCallback != null){
                    mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 1);
                    //Toast.makeText(v.getContext(), "TEST: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            } else*/

            if (v.getId() == buttonEditDetails.getId() || v.getId() == textViewEditDetails.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                }
            }

        }
    }
}
