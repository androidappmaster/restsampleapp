package es.appmaster.restapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.appmaster.restapp.R;
import es.appmaster.restapp.model.Student;

/**
 * Students adapter
 *
 * @author manolovn
 */
public class StudentsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Student> students;

    private LayoutInflater inflater;

    public StudentsAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.student_item, null);
            holder = new ViewHolder();
            holder.studentPhoto = (ImageView)convertView.findViewById(R.id.student_photo);
            holder.studentName = (TextView)convertView.findViewById(R.id.student_name);
            holder.studentCity = (TextView)convertView.findViewById(R.id.student_city);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Student student = students.get(position);

        if (student.getUrlPhoto() != null)
            Picasso.with(context).load(student.getUrlPhoto()).into(holder.studentPhoto);

        holder.studentName.setText(student.getName());
        holder.studentCity.setText(student.getCity());

        return convertView;
    }

    class ViewHolder {
        ImageView studentPhoto;
        TextView studentName;
        TextView studentCity;
    }

}
