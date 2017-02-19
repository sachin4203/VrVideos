package sachin.com.nanodegreefinalproject.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sachin.com.nanodegreefinalproject.R;


 //   https://gist.github.com/skyfishjy/443b7448f59be978bc59


public class VideoCursorAdapter extends CursorRecyclerViewAdapter<VideoCursorAdapter.ViewHolder> {

  private static Context mContext;
  private boolean isPercent;

  private OnItemClickListener listener;
  // Define the listener interface
  public interface OnItemClickListener {
    void onItemClick(View itemView, int position);
  }
  // Define the method that allows the parent activity or fragment to define the listener
  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }


  public VideoCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor);
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item, parent, false);
    ViewHolder vh = new ViewHolder(itemView);
    return vh;
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {
    viewHolder.title.setText(cursor.getString(cursor.getColumnIndex("title")));
    Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex("url_default"))).placeholder(R.drawable.photo_placeholder).into(viewHolder.thumbnail);

  }


  @Override
  public int getItemCount() {
    return super.getItemCount();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView title;
    public final ImageView thumbnail;

    public ViewHolder(final View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.title);
      thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Triggers click upwards to the adapter on click
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.onItemClick(itemView, position);
            }
          }
        }
      });

    }

  }
}
