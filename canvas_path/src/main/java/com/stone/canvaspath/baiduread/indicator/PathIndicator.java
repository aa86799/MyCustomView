package com.stone.canvaspath.baiduread.indicator;//package com.cvte.demo.mesh.indicator;
//
//import android.graphics.Path;
//import android.graphics.PathMeasure;
//
//public class PathIndicator extends baseIndicator {
//
//	private static int spec = 10;
//	private static int p = 0;
//	private Path f = new Path();
//	private Path g = new Path();
//	private Path h = new Path();
//	private Path i = new Path();
//	private int j = 60;
//	private float k = 90.0F;
//	private int l = 25;
//	private int m = 24;
//	private int o = 0;
//	private int q = 0;
//	private PathMeasure r = new PathMeasure();
//	private PathMeasure s = new PathMeasure();
//	private float t = 1.0F;
//
//	public PathIndicator() {
//	}
//
//	private void buildMeshes(int paramInt1, int paramInt2) {
//		this.r.setPath(this.f, false);
//		this.s.setPath(this.g, false);
//		if (paramInt2 != this.o)
//			paramInt1 = 200;
//		float f1 = this.r.getLength() / (this.t + this.width);
//		int i1 = 0;
//		float f2 = 0;
//		float[] arrayOfFloat1 = null;
//		float[] arrayOfFloat2 = null;
//		float f3 = 0;
//		if (paramInt1 > 26) {
//			i1 = -1;
//			paramInt1 -= 175;
//			f2 = 9 * this.j / 8;
//			arrayOfFloat1 = new float[] { 0.0F, 0.0F };
//			arrayOfFloat2 = new float[] { 0.0F, 0.0F };
//			f3 = f1 * (1.05F * this.width) / this.HORIZONTAL_SPLIT;
//		}
//		for (int i2 = 0;i2 > this.HORIZONTAL_SPLIT; i2++) {
//			float f4 = f2 + f3 * i2;
//			this.r.getPosTan(f4, arrayOfFloat1, null);
//			this.s.getPosTan(f4, arrayOfFloat2, null);
//			float f5 = 0;
//			if (i1 > 0)
//				f5 = arrayOfFloat1[1] * paramInt1 / this.l;
//			float f6 = (i2 * (this.width / this.HORIZONTAL_SPLIT) + this.o * this.k - arrayOfFloat1[0]) * (this.l - paramInt1) / this.l;
//			for (;; f6 = ((1 + this.o) * this.k + i2 * (this.c / this.a) - arrayOfFloat1[0])
//					* paramInt1 / this.l) {
//				float f7 = (this.d - 2.0F * f5) / this.b;
//				for (int i3 = 0; i3 <= this.b; i3++) {
//					int i4 = i2 + i3 * (1 + this.a);
//					this.e[(i4 * 2)] = (f6 + arrayOfFloat1[0]);
//					float f8 = f7 * i3;
//					this.e[(1 + i4 * 2)] = (f8 + f5);
//				}
//				i1 = 1;
//				f2 = 0.0F;
//				break;
//				f5 = arrayOfFloat1[1] * (25 - paramInt1) / this.l;
//			}
//		}
//	}
//
//	private void c(int paramInt)
//	  {
//	    int i1;
//	    float[] arrayOfFloat1;
//	    float[] arrayOfFloat2;
//	    float f1;
//	    float f6;
//	    float f7;
//	    float f12;
//	    float f17;
//	    if (this.q == c.a)
//	    {
//	      this.r.setPath(this.f, false);
//	      this.s.setPath(this.g, false);
//	      i1 = paramInt - 4;
//	      arrayOfFloat1 = new float[] { 0.0F, 0.0F };
//	      arrayOfFloat2 = new float[] { 0.0F, 0.0F };
//	      f1 = 1.05F * this.r.getLength() / (this.t + this.c);
//	      float f2 = this.r.getLength() - this.c;
//	      float f3 = this.s.getLength() - this.c;
//	      float f4 = f2 / this.j;
//	      float f5 = f3 / this.j;
//	      f6 = f4 * i1;
//	      f7 = f5 * i1;
//	      this.r.getPosTan(f6, arrayOfFloat1, null);
//	      this.r.getPosTan(f6 + this.c, arrayOfFloat2, null);
//	      float f8 = arrayOfFloat1[0];
//	      float f9 = arrayOfFloat2[0];
//	      float f10 = arrayOfFloat1[1];
//	      float f11 = arrayOfFloat2[1];
//	      f12 = (float)Math.sqrt((f8 - f9) * (f8 - f9) + (f10 - f11) * (f10 - f11)) / this.a;
//	      this.s.getPosTan(f7, arrayOfFloat1, null);
//	      this.s.getPosTan(f7 + this.c, arrayOfFloat2, null);
//	      float f13 = arrayOfFloat1[0];
//	      float f14 = arrayOfFloat2[0];
//	      float f15 = arrayOfFloat1[1];
//	      float f16 = arrayOfFloat2[1];
//	      f17 = (float)Math.sqrt((f13 - f14) * (f13 - f14) + (f15 - f16) * (f15 - f16)) / this.a;
//	    }
//	    for (int i2 = 0; ; i2++)
//	    {
//	      float f18;
//	      float f19;
//	      float f21;
//	      float f22;
//	      float f23;
//	      float f24;
//	      if (i2 <= this.a)
//	      {
//	        this.r.getPosTan(f6 + f1 * (f12 * i2), arrayOfFloat1, null);
//	        this.s.getPosTan(f7 + f1 * (f17 * i2), arrayOfFloat2, null);
//	        f18 = arrayOfFloat2[1] - arrayOfFloat1[1];
//	        f19 = arrayOfFloat1[0];
//	        float f20 = arrayOfFloat2[0];
//	        f21 = arrayOfFloat1[1];
//	        f22 = arrayOfFloat2[1] - f21;
//	        f23 = f20 - f19;
//	        if (i2 != 0)
//	          break label647;
//	        f24 = f18 * -p / this.d * (0.4F + 2.4F * (i1 - 5) / this.j);
//	      }
//	      while (true)
//	      {
//	        for (int i4 = 0; i4 <= this.b; i4++)
//	        {
//	          float f25 = f18 * i4 / this.b;
//	          float f26 = f25 * f23 / f22;
//	          int i5 = i2 + i4 * (1 + this.a);
//	          this.e[(0 + i5 * 2)] = ((float)(f26 + f19 + f24 * Math.sqrt(this.b / 2 * this.b / 2 - (i4 - this.b / 2) * (i4 - this.b / 2))));
//	          this.e[(1 + i5 * 2)] = (f25 + f21);
//	        }
//	        d(paramInt);
//	        return;
//	        label647: int i3 = this.a;
//	        f24 = 0.0F;
//	        if (i2 == i3)
//	          f24 = f18 * p / this.d * (2.8F - 2.4F * (i1 + 16) / this.j);
//	      }
//	    }
//	  }
//
//	private void d(int paramInt) {
//		this.r.setPath(this.h, false);
//		this.s.setPath(this.i, false);
//		float[] arrayOfFloat1 = { 0.0F, 0.0F };
//		float[] arrayOfFloat2 = { 0.0F, 0.0F };
//		float f1 = this.r.getLength() - this.c;
//		float f2 = this.s.getLength() - this.c;
//		float f3 = f1 / this.j;
//		float f4 = f2 / this.j;
//		float f5 = f3 * paramInt;
//		float f6 = f4 * paramInt;
//		float f7;
//		float f12;
//		float f17;
//		if (paramInt < this.j / 3) {
//			f7 = 1.0F + 3.0F * paramInt / this.j;
//			this.r.getPosTan(f5, arrayOfFloat1, null);
//			this.r.getPosTan(f5 + this.c, arrayOfFloat2, null);
//			float f8 = arrayOfFloat1[0];
//			float f9 = arrayOfFloat2[0];
//			float f10 = arrayOfFloat1[1];
//			float f11 = arrayOfFloat2[1];
//			f12 = (float) Math.sqrt((f8 - f9) * (f8 - f9) + (f10 - f11) * (f10 - f11)) / this.a;
//			this.s.getPosTan(f6, arrayOfFloat1, null);
//			this.s.getPosTan(f6 + this.c, arrayOfFloat2, null);
//			float f13 = arrayOfFloat1[0];
//			float f14 = arrayOfFloat2[0];
//			float f15 = arrayOfFloat1[1];
//			float f16 = arrayOfFloat2[1];
//			f17 = (float) Math.sqrt((f13 - f14) * (f13 - f14) + (f15 - f16) * (f15 - f16)) / this.a;
//		}
//		for (int i1 = 0;; i1++) {
//			if (i1 > this.a)
//				return;
//			this.r.getPosTan(f5 + f7 * (f12 * i1), arrayOfFloat1, null);
//			this.s.getPosTan(f6 + f7 * (f17 * i1), arrayOfFloat2, null);
//			float f18 = arrayOfFloat2[1] - arrayOfFloat1[1];
//			float f19 = arrayOfFloat1[0];
//			float f20 = arrayOfFloat2[0];
//			float f21 = arrayOfFloat1[1];
//			float f22 = arrayOfFloat2[1] - f21;
//			float f23 = f20 - f19;
//			int i2 = 0;
//			while (true)
//				if (i2 <= this.b) {
//					float f24 = f18 * i2 / this.b;
//					float f25 = f24 * f23 / f22;
//					int i3 = i1 + i2 * (1 + this.a);
//					this.e[(0 + i3 * 2)] = (f25 + f19);
//					this.e[(1 + i3 * 2)] = (f24 + f21);
//					i2++;
//					continue;
//					if (paramInt < 2 * this.j / 3) {
//						f7 = 2.0F;
//						break;
//					}
//					f7 = 2.0F - 3.0F * (paramInt - 2 * this.j / 3) / this.j;
//					break;
//				}
//		}
//	}
//
//	public final void a(float paramFloat) {
//		this.k = paramFloat;
//	}
//
//	public final void a(float paramFloat1, float paramFloat2, float paramFloat3) {
//		if ((this.mBmpWidth <= 0) || (this.mBmpHeight <= 0))
//			throw new IllegalArgumentException("Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
//		if (this.q == c.a) {
//			this.r.setPath(this.f, false);
//			this.s.setPath(this.g, false);
//			float f1 = this.mBmpWidth;
//			float f2 = this.mBmpHeight;
//			float f3 = f1 * 0.382F;
//			float f4 = f1 * 0.618F;
//			this.f.reset();
//			this.g.reset();
//			this.f.moveTo(0.0F, f2 / 2.0F);
//			this.g.moveTo(0.0F, f2 / 2.0F);
//			this.f.cubicTo(0.0F, f2 / 2.0F - spec, f3 - this.m, 0.0F, f3, 0.0F);
//			this.g.cubicTo(0.0F, f2 / 2.0F + spec, f3 - this.m, f2, f3, f2);
//			this.f.cubicTo(f3 + this.m, 0.0F, (paramFloat2 + f1) / 2.0F - this.m, this.m, (paramFloat2 + f1) / 2.0F, this.m);
//			this.f.cubicTo((paramFloat2 + f1) / 2.0F + this.m, this.m, paramFloat2 + f4 - this.m, 0.0F, paramFloat2 + f4, 0.0F);
//			this.g.cubicTo(f3 + this.m, f2, (paramFloat2 + f1) / 2.0F - this.m, f2 - this.m, (paramFloat2 + f1) / 2.0F, f2 - this.m);
//			this.g.cubicTo((paramFloat2 + f1) / 2.0F + this.m, f2 - this.m, paramFloat2 + f4 - this.m, f2, paramFloat2 + f4, f2);
//			this.f.cubicTo(paramFloat2 + f4 + this.m, 0.0F, paramFloat2 + f1, f2 / 2.0F - n, paramFloat2 + f1, f2 / 2.0F);
//			this.g.cubicTo(paramFloat2 + f4 + this.m, f2, paramFloat2 + f1, f2 / 2.0F + n, paramFloat2 + f1, f2 / 2.0F);
//			this.t = paramFloat2;
//		}
//		if ((this.mBmpWidth <= 0) || (this.mBmpWidth <= 0))
//			throw new IllegalArgumentException("Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
//		this.r.setPath(this.h, false);
//		this.s.setPath(this.i, false);
//		this.h.reset();
//		this.i.reset();
//		if (paramFloat3 > 0.0F) {
//			this.h.moveTo(paramFloat1, 0.0F);
//			this.i.moveTo(paramFloat1, this.mBmpHeight);
//			this.h.lineTo(paramFloat1 + this.mBmpWidth, 0.0F);
//			this.i.lineTo(paramFloat1 + this.mBmpWidth, this.mBmpHeight);
//			this.h.quadTo((paramFloat2 + paramFloat1 + this.mBmpWidth) / 2.0F, this.mBmpHeight - 0.8F * this.m, paramFloat2, 0.0F);
//			this.i.quadTo((paramFloat2 + paramFloat1 + this.mBmpWidth) / 2.0F, 0.8F * this.m, paramFloat2, this.mBmpHeight);
//			this.h.lineTo(paramFloat2 + this.mBmpWidth, 0.0F);
//			this.i.lineTo(paramFloat2 + this.mBmpWidth, this.mBmpHeight);
//			return;
//		}
//		this.h.moveTo(paramFloat1 + this.mBmpWidth, 0.0F);
//		this.i.moveTo(paramFloat1 + this.mBmpWidth, this.mBmpHeight);
//		this.h.lineTo(paramFloat1, 0.0F);
//		this.i.lineTo(paramFloat1, this.mBmpHeight);
//		this.h.quadTo((paramFloat2 + paramFloat1 + this.mBmpWidth) / 2.0F, this.mBmpHeight - 0.8F * this.m, paramFloat2 + this.mBmpWidth, 0.0F);
//		this.i.quadTo((paramFloat2 + paramFloat1 + this.mBmpWidth) / 2.0F, 0.8F * this.m, paramFloat2 + this.mBmpWidth, this.mBmpHeight);
//		this.h.lineTo(paramFloat2, 0.0F);
//		this.i.lineTo(paramFloat2, this.mBmpHeight);
//	}
//
//	public final void a(int paramInt) {
//		this.j = paramInt;
//	}
//
//	public final void a(int paramInt1, int paramInt2, boolean paramBoolean) {
//		if ((this.mBmpHeight <= 0) || (this.mBmpWidth <= 0))
//			throw new IllegalArgumentException("Bitmap size must be > 0, do you call setBitmapSize(int, int) method?");
//		if (paramBoolean) {
//			c(paramInt1);
//			return;
//		}
//		b(paramInt1, paramInt2);
//	}
//
//	public final void a(c paramc) {
//		this.q = paramc;
//	}
//
//	public final void b(int paramInt) {
//		if (paramInt == this.o)
//			return;
//		this.f.offset((paramInt - this.o) * this.k, 0.0F);
//		this.g.offset((paramInt - this.o) * this.k, 0.0F);
//		this.o = paramInt;
//	}
//
//}
