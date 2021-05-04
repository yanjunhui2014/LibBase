package com.milo.libbase.widget.refreshview;

import com.milo.libbase.R;

public class HorizontalMoreViewHolder extends VerticalMoreViewHolder {

    public HorizontalMoreViewHolder() {
        super();
    }

    public HorizontalMoreViewHolder(String textEmpty, String textError) {
        super(textEmpty, textError);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.libbase_framework_view_more_horizontal;
    }

}
