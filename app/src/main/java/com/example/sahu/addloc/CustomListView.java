    package com.example.sahu.addloc;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.animation.DecelerateInterpolator;
    import android.widget.ArrayAdapter;
    import android.widget.TextView;

    /**
     *
     */
    public class CustomListView extends ArrayAdapter{

        private int mLastPosition;

        protected Context mContext;
        String[] mContactName;
        String[] mContactNumber;
        String[] mBigName;

        public CustomListView(Context context, String[] ContactNumber, String[] ContactName) {
            super(context, R.layout.map_list_item, ContactName);
            mContext = context;
            mContactName = ContactNumber;
            mContactNumber = ContactName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.map_list_item, null);

                holder = new ViewHolder();


                holder.mBigName = (TextView) convertView.findViewById(R.id.BigName);
                holder.mContactName = (TextView) convertView.findViewById(R.id.ContactName);
                holder.mContactNumber = (TextView) convertView.findViewById(R.id.ContactNumber);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.mContactName.setText(mContactName[position]);
            holder.mContactNumber.setText(mContactNumber[position]);
            holder.mBigName.setText(mContactName[position].substring(0,1).toUpperCase());


            /**
             *  Animate List View like a card Stack
             *
             *
             */
            float initialTranslation = (mLastPosition <= position ? 500f : -500f);

            convertView.setTranslationY(initialTranslation);
            convertView.animate()
                    .setInterpolator(new DecelerateInterpolator(1.0f))
                    .translationY(0f)
                    .setDuration(300l)
                    .setListener(null);

            // Keep track of the last position we loaded
            mLastPosition = position;

            //return View
            return convertView;
        }

        private static class ViewHolder {
            TextView mBigName;
            TextView mContactName;
            TextView mContactNumber;
        }

    }
