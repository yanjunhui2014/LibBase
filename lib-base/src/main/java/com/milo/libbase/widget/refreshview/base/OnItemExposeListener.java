package com.milo.libbase.widget.refreshview.base;

public abstract class OnItemExposeListener {

    public abstract void onItemViewVisible(boolean visible, int position);

    protected float visiblePercent() {
        return 1;
    }

    public void setExposeItemVisible(ExposeItem exposeItem, boolean visible, int position) {
        if (exposeItem.isVisible()) {
            if (!visible) {
                exposeItem.setVisible(false);
                trackExpose(false, position);
            }
        } else {
            if (visible) {
                exposeItem.setVisible(true);
                if (!exposeItem.isTracked()) {
                    exposeItem.setTracked(true);
                    trackExpose(true, position);
                }
            }
        }
    }

    protected abstract void trackExpose(boolean visible, int position);

    public interface ExposeItem {

        boolean isVisible();

        void setVisible(boolean visible);

        boolean isFirst();

        void setFirst(boolean isFirst);

        boolean isTracked();

        void setTracked(boolean tracked);
    }

    public static class BaseExposeItem implements ExposeItem {

        private boolean mIsItemVisible;
        private boolean mIsItemFirst = true;
        private boolean mIsChecked;

        @Override
        public boolean isVisible() {
            return mIsItemVisible;
        }

        @Override
        public void setVisible(boolean visible) {
            mIsItemVisible = visible;
        }

        @Override
        public boolean isFirst() {
            return mIsItemFirst;
        }

        @Override
        public void setFirst(boolean isItemFirst) {
            mIsItemFirst = isItemFirst;
        }

        @Override
        public boolean isTracked() {
            return mIsChecked;
        }

        @Override
        public void setTracked(boolean tracked) {
            mIsChecked = tracked;
        }
    }

}
