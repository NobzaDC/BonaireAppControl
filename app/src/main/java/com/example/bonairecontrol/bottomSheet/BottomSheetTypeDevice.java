package com.example.bonairecontrol.bottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.bonairecontrol.Controlador_adaptador.TypeDeviceAdapter;
import com.example.bonairecontrol.Modelos.ModelTypeDevices;
import com.example.bonairecontrol.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetTypeDevice extends BottomSheetDialogFragment implements AdapterView.OnItemClickListener {

    ListView lvTypeDevices;
    TypeDeviceAdapter adapter;

    private BottomSheetTypeDeviceListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_type_devices, container, false);

        lvTypeDevices = view.findViewById(R.id.lv_type_devices);

        List<ModelTypeDevices> mList = new ArrayList<>();

        mList.add(new ModelTypeDevices(R.drawable.eliminar_humo_ico, "Bonaire-BQH"));
        mList.add(new ModelTypeDevices(R.drawable.bqd, "Bonaire-BQD"));
        mList.add(new ModelTypeDevices(R.drawable.bqv, "Bonaire-BQV"));
        mList.add(new ModelTypeDevices(R.drawable.b_tech, "Bonaire-TECH"));
        mList.add(new ModelTypeDevices(R.drawable.b_tech, "COCINAS INTELIGENTES"));

        adapter = new TypeDeviceAdapter(getContext(), R.layout.list_type_devices, mList);
        lvTypeDevices.setAdapter(adapter);
        lvTypeDevices.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetTypeDevice.BottomSheetTypeDeviceListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "Debe implementar listener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = adapter.getItem(position).getNameType();
        listener.onFieldTypeClicked(name);
        dismiss();
    }

    public interface BottomSheetTypeDeviceListener{
        void onFieldTypeClicked(String mac_name);
    }
}