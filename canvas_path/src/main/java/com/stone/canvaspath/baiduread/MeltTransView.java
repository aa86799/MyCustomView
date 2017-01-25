package com.stone.canvaspath.baiduread;//package com.cvte.demo.mesh;
//
//import com.cvte.demo.mesh.indicator.PathIndicator;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//
//public class MeltTransView extends View {
//	private boolean a = false;
//	private Matrix b = new Matrix();
//	private final Paint c = new Paint(1);
//	private float d;
//	private float e;
//	private int f = 400;
//	private int g = 0;
//	private boolean h = false;
//	private int i;
//	private Bitmap j;
//	private PathIndicator k = null;
//	private int l = 1;
//	private ViewPager m;
//	private int n;
//	private int o;
//	private float p;
//	private e q = new e();
//
//	public MeltTransView(Context paramContext) {
//		super(paramContext);
//		this.c.setStrokeWidth(2.0F);
//	}
//
//	private void a() {
//		ColorFilter localColorFilter = this.q.a(this.o, this.p);
//		this.c.setColorFilter(localColorFilter);
//	}
//
//	private void b() {
//		if ((getWidth() <= 0) || (getHeight() <= 0))
//			;
//		while (this.m == null)
//			return;
//		d locald = (d) this.m.getAdapter();
//		int i1 = this.m.getAdapter().getCount();
//		int i2 = (int) TypedValue.applyDimension(1, 8.0F, getContext().getResources().getDisplayMetrics());
//		int i3 = (int) TypedValue.applyDimension(1, 5.0F, getContext().getResources().getDisplayMetrics());
//		int i4 = getWidth() / i1 - i3 * 2;
//		int i5 = getHeight() - i2 * 2;
//		this.d = i4;
//		this.e = i5;
//		this.j = BitmapFactory.decodeResource(getResources(), 2130838094);
//		byte[] arrayOfByte = this.j.getNinePatchChunk();
//		boolean bool = NinePatch.isNinePatchChunk(this.j.getNinePatchChunk());
//		int i6 = 0;
//		if (bool) {
//			Bitmap localBitmap = Bitmap.createBitmap(i4, i5, Bitmap.Config.ARGB_8888);
//			Canvas localCanvas = new Canvas(localBitmap);
//			Rect localRect = new Rect(0, 0, i4, i5);
//			new NinePatch(this.j, arrayOfByte, null).draw(localCanvas, localRect);
//			this.j = localBitmap;
//		}
//		while (i6 < i1) {
//			this.q.a(locald.a(i6));
//			i6++;
//		}
//		a();
//	}
//
//	public final void a(int paramInt) {
//		this.n = paramInt;
//		if (paramInt == 1) {
//			this.h = true;
//			this.l = 1;
//		}
//		while (true) {
//			this.k.a(c.a);
//			this.k.a(this.f);
//			return;
//			this.h = false;
//		}
//	}
//
//	public final void a(int paramInt, float paramFloat) {
//		this.o = paramInt;
//		this.p = paramFloat;
//		invalidate();
//	}
//
//	public final void a(ViewPager paramViewPager) {
//		this.m = paramViewPager;
//		b();
//	}
//
//	public final void b(int paramInt) {
//		this.l = Math.abs(paramInt - this.o);
//		if (this.l > 1) {
//			float f1 = this.o * this.i;
//			float f2 = paramInt * this.i;
//			this.k.a(c.b);
//			this.k.a(this.i * this.l);
//			b localb = this.k;
//			localb.a(f1, f2, paramInt - this.o);
//			Animation localAnimation = getAnimation();
//			if ((localAnimation == null) || (localAnimation.hasEnded())) {
//				MeltTransAnimation localMeltTransAnimation = new MeltTransAnimation(this.i * this.l);
//				localMeltTransAnimation.a(new g(this));
//				localMeltTransAnimation.setDuration(400L);
//				startAnimation(localMeltTransAnimation);
//			}
//			if (this.n == 0) {
//				this.o = paramInt;
//				this.p = 0.0F;
//			}
//			this.k.b(paramInt);
//			a();
//		}
//		do {
//			return;
//			this.k.a(c.a);
//			this.k.a(this.f);
//		} while (this.n != 0);
//		this.o = paramInt;
//		this.p = 0.0F;
//		invalidate();
//	}
//
//	protected void onDraw(Canvas paramCanvas)
//	  {
//	    Long.valueOf(System.currentTimeMillis());
//	    super.onDraw(paramCanvas);
//	    paramCanvas.concat(this.b);
//	    if (this.m == null)
//	      return;
//	    if (this.l <= 1)
//	    {
//	      if ((!this.h) && (this.g >= this.o + this.p))
//	        break label186;
//	      this.k.b(this.o);
//	      this.g = (1 + this.o);
//	    }
//	    for (this.h = false; (this.p - 0.125F > 1.E-005D) && (this.p - 0.875F < 1.E-005D); this.h = false)
//	    {
//	      label88: float f1 = this.i * this.p;
//	      this.k.a((int)f1, this.o, true);
//	      a();
//	      paramCanvas.drawBitmapMesh(this.j, this.k.a(), this.k.b(), this.k.c(), 0, null, 0, this.c);
//	      return;
//	      label186: if ((!this.h) && (this.g <= this.o + this.p))
//	        break label88;
//	      this.k.b(this.o);
//	      this.g = (-1 + this.o);
//	    }
//	    this.k.a((int)(200.0F * this.p), this.o, false);
//	    a();
//	    paramCanvas.drawBitmapMesh(this.j, this.k.a(), this.k.b(), this.k.c(), 0, null, 0, this.c);
//	  }
//
//	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
//		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
//		if (paramBoolean) {
//			b();
//			this.i = ((getWidth() - getPaddingLeft() - getPaddingRight()) / this.m.getAdapter().getCount());
//			this.f = this.i;
//			int i1 = (int) ((getHeight() - this.e) / 2.0F);
//			int i2 = (int) ((this.i - this.d) / 2.0F);
//			this.b.setTranslate(i2, i1);
//			this.k.a((int) this.d, (int) this.e);
//			this.k.a(this.f);
//			this.k.a(this.i);
//			b localb = this.k;
//			float f1 = this.i;
//			localb.a(0.0F, f1, 1.0F);
//			this.k.a(this.d, this.e);
//			if (!this.a) {
//				b(this.m.getCurrentItem());
//				this.a = true;
//			}
//		}
//	}
//}
