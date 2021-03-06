package org.kohsuke.lego.g2l.ldraw;

import org.apache.sanselan.color.ColorCIELab;

import static org.apache.sanselan.color.ColorConversions.*;

/**
 * Color code in LDraw data file
 *
 * @author Kohsuke Kawaguchi
 */
// see http://www.ldraw.org/article/547
public enum Color {
    BLACK       ( 0,"05131D"),
    BLUE        ( 1,"0055BF"),
    GREEN       ( 2,"257A3E"),
    RED         ( 4,"C91A09"),
    BROWN       ( 6,"583927"),
    LIGHT_GRAY  ( 7,"9BA19D"),
    DARK_GRAY   ( 8,"6D6E5C"),
    LIGHT_BLUE  ( 9,"B4D2E3"),
    WHITE       (15,"FFFFFF"),
    TAN         (19,"E4CD9E"),
    ORANGE      (25,"FE8A18"),
    LIGHT_GREEN (27,"BBE90B")
    ;

    public final int id;
    public final int rgb;
    public final ColorCIELab cie;

    Color(int id, String code) {
        this.id = id;
        this.rgb = Integer.parseInt(code, 16);
        this.cie = convertXYZtoCIELab(convertRGBtoXYZ(rgb));
    }

    public static Color nearest(int rgb) {
        ColorCIELab cie = convertXYZtoCIELab(convertRGBtoXYZ(rgb));

        double best=Double.MAX_VALUE;
        Color nearest=null;

        for (Color e : Color.values()) {
            double d = distance(e.cie, cie);
            if (d<best) {
                best = d;
                nearest = e;
            }
        }
        return nearest;
    }

    public int r() {
        return rgb>>16;
    }

    public int g() {
        return (rgb>>8)&0xFF;
    }

    public int b() {
        return rgb&0xFF;
    }

    public double distance(Color that) {
        return sq(this.r()-that.r()) + sq(this.g()-that.g()) + sq(this.b()-that.b());
//        return distance(this.cie,that.cie);
    }

    static double distance(ColorCIELab lhs, ColorCIELab rhs) {
        return sq(lhs.L - rhs.L)
             + sq(lhs.a - rhs.a)
             + sq(lhs.b - rhs.b);
    }

    static double sq(double d) {
        return d * d;
    }
}
