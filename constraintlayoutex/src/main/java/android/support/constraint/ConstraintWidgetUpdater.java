package android.support.constraint;

public class ConstraintWidgetUpdater {
    public static void updateWidget(ConstraintLayout.LayoutParams lp,
                                    int left, int top, int right, int bottom) {
        lp.widget.setDrawX(left);
        lp.widget.setDrawY(top);
        lp.widget.setFrame(left, top, right, bottom);
    }
}
