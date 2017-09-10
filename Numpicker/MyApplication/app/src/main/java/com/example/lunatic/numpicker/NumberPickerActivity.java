package com.example.lunatic.numpicker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by liufeng on 2017/9/10.
 */

public class NumberPickerActivity extends Activity implements View.OnClickListener,
        NumberPicker.OnScrollListener, NumberPicker.OnValueChangeListener  {
    private TextView titleView;
    private NumberPicker numberPicker;
    //private List<Tags> optionList;
    private List<String> optionTitleList;
    private int chooseIndex;
    private String title;
    private View viewNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_number_dialog);
//        Intent intent = this.getIntent();        //获取已有的intent对象
//        Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
    //    title = bundle.getString(IntentConstants.PARAM_OPTION_TITLE);
      //  String options = bundle.getString(IntentConstants.PARAM_OPTION_LIST);
//        if (StringUtils.isNotBlank(options)) {
//            optionList = (List<Tags>) JsonUtil.getInstance().deserialize(options, List.class, Tags.class);
//        }
       // chooseIndex = bundle.getInt(IntentConstants.PARAM_OPTION_SELECT_INDEX, 0);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initView();
        initPicker();
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        titleView = (TextView) findViewById(R.id.title);
        numberPicker = (NumberPicker) findViewById(R.id.number_choose);
        viewNumber=findViewById(R.id.number_view);
        titleView.setText(title);
        setNumberPickerDividerColorAndHeight(numberPicker);
        setNumberPickerTextColor(numberPicker, Color.BLACK);
        viewNumber.setOnClickListener(this);

    }


    public void initPicker() {
//        optionTitleList = new ArrayList<>();
//        for (Tags tags : optionList) {
//            optionTitleList.add(tags.getName());
//        }
        String[] optionList={"一月","二月","三月"};
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(optionList.length - 1);
        numberPicker.setValue(chooseIndex);
        numberPicker.setWrapSelectorWheel(true);//是否循环滚动
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(optionList);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                chooseIndex = picker.getValue();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back||v.getId()==R.id.number_view) {
            Intent intent = new Intent(NumberPickerActivity.this, MainActivity.class);
            setResult(2, intent);
            this.finish();
        } else if (v.getId() == R.id.sure) {
            Intent intent = new Intent(NumberPickerActivity.this, MainActivity.class);
           // intent.putExtra(IntentConstants.PARAM_OPTION_SELECT_INDEX, chooseIndex);
            setResult(2, intent);
            finish();
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }


    private void setNumberPickerDividerColorAndHeight(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.colorAccent)));

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
        for (Field pf2 : pickerFields) {
            if (pf2.getName().equals("mSelectionDividerHeight")) {
                pf2.setAccessible(true);
                try {
                    int result = 1;
                    pf2.set(picker, result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}


