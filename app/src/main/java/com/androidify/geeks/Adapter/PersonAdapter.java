package com.androidify.geeks.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidify.geeks.BusinessObjects.Person;
import com.androidify.geeks.R;

import java.util.ArrayList;

public class PersonAdapter extends ArrayAdapter<Person> {

    private int layoutResource;
    private LayoutInflater inflater;
    private float density = 2f;
    private ListView listView;
    private Context mContext;

    public PersonAdapter(Context context, int resource, ArrayList<Person> items) {
        super(context, resource, items);
        mContext = context;
        layoutResource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View workingView = null;

        if (convertView == null) {
            workingView = inflater.inflate(layoutResource, null);
        } else {
            workingView = convertView;
        }

        final PersonHolder holder = getPersonHolder(workingView);
        final Person entry = getItem(position);

        ((TextView) holder.mainView.findViewById(R.id.mainText)).setText(entry.getName());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);
        workingView.setOnTouchListener(new SwipeListItemDetector(holder, position));

        return workingView;
    }

    private PersonHolder getPersonHolder(View workingView) {
        Object tag = workingView.getTag();
        PersonHolder holder = null;

        if (tag == null || !(tag instanceof PersonHolder)) {
            holder = new PersonHolder();
            holder.mainView = (LinearLayout) workingView.findViewById(R.id.displayView);
            holder.deleteView = (RelativeLayout) workingView.findViewById(R.id.deleteView);
            holder.bookmarkView = (RelativeLayout) workingView.findViewById(R.id.bookmarkView);
            
            workingView.setTag(holder);
        } else {
            holder = (PersonHolder) tag;
        }

        return holder;
    }

    public static class PersonHolder {
        public LinearLayout mainView;
        public RelativeLayout deleteView;
        public RelativeLayout bookmarkView;
        
        /* other views here */
    }

    public void setListView(ListView view) {
        listView = view;
    }

    public class SwipeListItemDetector implements View.OnTouchListener {

        private final String TAG = SwipeListItemDetector.class.getSimpleName();
        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private PersonHolder holder;
        private int position;

        public SwipeListItemDetector(PersonHolder h, int pos) {
            holder = h;
            position = pos;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

                    if (deltaX > 0) {
                        holder.deleteView.setVisibility(View.GONE);
                    } else {
                        // if first swiped left and then swiped right
                        holder.deleteView.setVisibility(View.VISIBLE);
                    }

                    swipe(-(int) deltaX);
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    Log.e(TAG, "deltaX ----------: " + deltaX + ": MIN_DISTANCE : " + MIN_DISTANCE);


                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        Log.e(TAG, "Inside swipe remove: deltaX : " + Math.abs(deltaX) + ": MIN_DISTANCE : " + MIN_DISTANCE);

                        if (deltaX < 0) {
                            // swipe left
                            Log.e(TAG, "Delta value right to left : " + deltaX);
                            removeListItem();
                        } else {
                            // swipe to right
                            Log.e(TAG, "Delta value left to right : " + deltaX);
                            swipe(0);
                        }

                    } else {
                        Log.e(TAG, "else deltaX : " + Math.abs(deltaX) + ": MIN_DISTANCE : " + MIN_DISTANCE);
                        swipe(0);
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

                    holder.deleteView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    holder.deleteView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipe(int distance) {
            View animationView = holder.mainView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

        private void removeListItem() {
            remove(getItem(position));
            notifyDataSetChanged();
        }
    }
}