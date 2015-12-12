package com.stone.pintu.view.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * author : stone
 * email  : aa86799@163.com
 * time   : 15/8/10 15 40
 */
public class ImageSplitterUtil {

    /**
     * 将传入的bitmap切成pieces份
     * @param bitmap
     * @param pieces
     * @return
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int pieces) {
        List<ImagePiece> list = new ArrayList<>();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        /*
        要求切成一个正方形  p*p
         */
        int pieceWidth = Math.min(w, h) / pieces; //item宽度
        ImagePiece imagePiece;
        for (int i = 0; i < pieces; i++) {
            for (int j = 0; j < pieces; j++) {
                imagePiece = new ImagePiece();
                imagePiece.index = j + i * pieces;
                int x = j * pieceWidth;
                int y = i * pieceWidth;
                imagePiece.bitmap = Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth);
                list.add(imagePiece);
            }
        }

        return list;
    }
}
