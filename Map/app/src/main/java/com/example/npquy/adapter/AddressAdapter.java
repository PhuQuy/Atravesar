package com.example.npquy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.npquy.activity.R;
import com.example.npquy.entity.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ITACHI on 3/19/2016.
 */
public class AddressAdapter extends ArrayAdapter<Address>{
    Activity context=null;
    ArrayList<Address> myArray=null;
    int layoutId;


    public AddressAdapter(Activity context,
                          int layoutId,
                          ArrayList<Address>arr) {
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.myArray=arr;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater=
                context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);
        //chỉ là test thôi, bạn có thể bỏ If đi
        if(myArray.size()>0 && position>=0)
        {
            final TextView roadName=(TextView)
                convertView.findViewById(R.id.road_name);
            final TextView addressName=(TextView)
                    convertView.findViewById(R.id.address);

            final Address address = myArray.get(position);
            if(address != null) {
                String fullAddress = address.getFulladdress();
                if (!fullAddress.isEmpty()) {
                    String[] data = fullAddress.split(",");
                    roadName.setText(data[0]);
                    try {
                        addressName.setText(data[2]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        if (data.length == 1) {
                            addressName.setText("");
                        } else {
                            addressName.setText(data[1]);
                        }
                    } catch (Exception e) {
                        addressName.setText("");
                    }
                } else {
                    roadName.setText("Unknown");
                }
            }
            //lấy ImageView ra để thiết lập hình ảnh cho đúng
            final ImageView imgitem =(ImageView)
                    convertView.findViewById(R.id.imgitem);
        }
        //Vì View là Object là dạng tham chiếu đối tượng, nên
        //mọi sự thay đổi của các object bên trong convertView
        //thì nó cũng biết sự thay đổi đó
        return convertView;//trả về View này, tức là trả luôn
        //về các thông số mới mà ta vừa thay đổi
    }

    public AddressAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public AddressAdapter(Context context, int resource, Address[] objects) {
        super(context, resource, objects);
    }

    public AddressAdapter(Context context, int resource, int textViewResourceId, Address[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AddressAdapter(Context context, int resource, List<Address> objects) {
        super(context, resource, objects);
    }

    public AddressAdapter(Context context, int resource, int textViewResourceId, List<Address> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
