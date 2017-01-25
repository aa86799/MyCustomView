package com.stone.canvaspath.baiduread;//package com.cvte.demo.mesh;
//
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//public class MeltTransIndicator extends FrameLayout {
//
//	 private LinearLayout a;
//	  private MeltTransView b;
//	  private ViewPager c;
//	  private ViewPager.OnPageChangeListener d;
//	  private int e;
//	  private int f = 0;
//
//	  public MeltTransIndicator(Context paramContext)
//	  {
//	    super(paramContext);
//	  }
//
//	  public MeltTransIndicator(Context paramContext, AttributeSet paramAttributeSet)
//	  {
//	    super(paramContext, paramAttributeSet);
//	  }
//
//	  private void b()
//	  {
//	    if ((getWidth() <= 0) || (getHeight() <= 0));
//	    while (this.c == null)
//	      return;
//	    d locald = (d)this.c.getAdapter();
//	    int i = this.c.getAdapter().getCount();
//	    removeAllViews();
//	    int j = getWidth() - getPaddingLeft() - getPaddingRight();
//	    this.b = new MeltTransView(getContext());
//	    this.b.a(this.c);
//	    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(j, getHeight());
//	    addView(this.b, localLayoutParams1);
//	    int k = (int)TypedValue.applyDimension(1, 8.0F, getContext().getResources().getDisplayMetrics());
//	    int m = j / i - k * 2;
//	    int n = getHeight() - k * 2;
//	    this.a = new LinearLayout(getContext());
//	    this.a.setOrientation(0);
//	    int i1 = 0;
//	    if (i1 < i)
//	    {
//	      RelativeLayout localRelativeLayout = new RelativeLayout(getContext());
//	      YueduText localYueduText = new YueduText(getContext());
//	      localYueduText.setText(locald.getPageTitle(i1));
//	      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
//	      localLayoutParams.addRule(13);
//	      localRelativeLayout.addView(localYueduText, localLayoutParams);
//	      if (locald.b(i1))
//	      {
//	        a.a();
//	        Drawable localDrawable = a.b().getResources().getDrawable(2130838541);
//	        a.a();
//	        localYueduText.setCompoundDrawablePadding(g.a(a.b(), 5.0F));
//	        localYueduText.setCompoundDrawablesWithIntrinsicBounds(null, null, localDrawable, null);
//	      }
//	      while (true)
//	      {
//	        localYueduText.setTextColor(Color.parseColor("#6f6b65"));
//	        localRelativeLayout.setOnClickListener(new f(this, i1));
//	        LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(m, n, 1.0F);
//	        localLayoutParams2.setMargins(0, k, 0, k);
//	        this.a.addView(localRelativeLayout, localLayoutParams2);
//	        i1++;
//	        break;
//	        localYueduText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//	      }
//	    }
//	    View localView1 = this.a.getChildAt(this.f);
//	    if ((localView1 instanceof RelativeLayout))
//	    {
//	      View localView2 = ((RelativeLayout)localView1).getChildAt(0);
//	      if ((localView2 instanceof YueduText))
//	        ((YueduText)localView2).setTextColor(-1);
//	    }
//	    FrameLayout.LayoutParams localLayoutParams3 = new FrameLayout.LayoutParams(j, getHeight());
//	    addView(this.a, localLayoutParams3);
//	  }
//
//	  public final LinearLayout a()
//	  {
//	    return this.a;
//	  }
//
//	  public final void a(int paramInt)
//	  {
//	    if (this.c == null)
//	      throw new IllegalStateException("ViewPager has not been bound.");
//	    if (Math.abs(paramInt - this.c.getCurrentItem()) <= 1)
//	      this.c.setCurrentItem(paramInt);
//	    while (true)
//	    {
//	      this.f = paramInt;
//	      return;
//	      this.c.setCurrentItem(paramInt, false);
//	    }
//	  }
//
//	  public final void a(ViewPager.OnPageChangeListener paramOnPageChangeListener)
//	  {
//	    this.d = paramOnPageChangeListener;
//	  }
//
//	  public final void a(ViewPager paramViewPager)
//	  {
//	    if (this.c == paramViewPager)
//	      return;
//	    if (this.c != null)
//	      this.c.setOnPageChangeListener(null);
//	    if (paramViewPager.getAdapter() == null)
//	      throw new IllegalStateException("ViewPager does not have adapter instance.");
//	    this.c = paramViewPager;
//	    this.c.setOnPageChangeListener(this);
//	    b();
//	  }
//
//	  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
//	  {
//	    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
//	    if (paramBoolean)
//	      b();
//	  }
//
//	  public void onPageScrollStateChanged(int paramInt)
//	  {
//	    if (this.b != null)
//	      this.b.a(paramInt);
//	    this.e = paramInt;
//	    if (this.d != null)
//	      this.d.onPageScrollStateChanged(paramInt);
//	  }
//
//	  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
//	  {
//	    if (this.b != null)
//	      this.b.a(paramInt1, paramFloat);
//	    this.f = paramInt1;
//	    if (this.d != null)
//	      this.d.onPageScrolled(paramInt1, paramFloat, paramInt2);
//	  }
//
//	  public void onPageSelected(int paramInt)
//	  {
//	    if (this.b != null)
//	      this.b.b(paramInt);
//	    if (this.e == 0)
//	      this.f = paramInt;
//	    d locald = (d)this.c.getAdapter();
//	    if (this.d != null)
//	      this.d.onPageSelected(paramInt);
//	    boolean bool = locald.b(paramInt);
//	    if (this.a != null)
//	    {
//	      int i = this.a.getChildCount();
//	      for (int j = 0; j < i; j++)
//	      {
//	        View localView3 = this.a.getChildAt(j);
//	        if ((localView3 instanceof RelativeLayout))
//	        {
//	          View localView4 = ((RelativeLayout)localView3).getChildAt(0);
//	          if ((localView4 instanceof YueduText))
//	            ((YueduText)localView4).setTextColor(Color.parseColor("#6f6b65"));
//	        }
//	      }
//	      if ((paramInt >= 0) && (paramInt < i))
//	        break label157;
//	    }
//	    label157: View localView2;
//	    do
//	    {
//	      View localView1;
//	      do
//	      {
//	        return;
//	        localView1 = this.a.getChildAt(paramInt);
//	      }
//	      while (!(localView1 instanceof RelativeLayout));
//	      localView2 = ((RelativeLayout)localView1).getChildAt(0);
//	    }
//	    while (!(localView2 instanceof YueduText));
//	    YueduText localYueduText = (YueduText)localView2;
//	    localYueduText.setTextColor(-1);
//	    if (bool)
//	    {
//	      a.a();
//	      Drawable localDrawable = a.b().getResources().getDrawable(2130837930);
//	      a.a();
//	      localYueduText.setCompoundDrawablePadding(g.a(a.b(), 4.0F));
//	      localYueduText.setCompoundDrawablesWithIntrinsicBounds(null, null, localDrawable, null);
//	      return;
//	    }
//	    localYueduText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//	  }
//
//	  public void onRestoreInstanceState(Parcelable paramParcelable)
//	  {
//	    SavedState localSavedState = (SavedState)paramParcelable;
//	    super.onRestoreInstanceState(localSavedState.getSuperState());
//	    this.f = localSavedState.a;
//	    requestLayout();
//	  }
//
//	  public Parcelable onSaveInstanceState()
//	  {
//	    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
//	    localSavedState.a = this.f;
//	    return localSavedState;
//	  }
//}
