/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npcgame;

import java.util.List;

/**
 *
 * @author millerti
 */
public class Property {
    enum PropType {
        NULL,
        INT,
        REAL,
        STR,
        BOOL,
        VEC
    }
    
//    static class IntProp extends Property {
//        
//    }
//    
//    static class BoolProp extends Property {
//        
//    }
//
//    static class StrProp extends Property {
//        
//    }
//
//    static class RealProp extends Property {
//        
//    }
//
//    static class VecProp extends Property {
//        
//    }
    
    Property() { type = PropType.NULL; }
    Property(long i) {
        type = PropType.INT;
        ival = i;
    }
    Property(double d) {
        type = PropType.REAL;
        vec = new double[1];
        vec[0] = d;
    }
    Property(double[] v) {
        type = PropType.VEC;
        vec = v;
    }
    Property(String s) {
        type = PropType.STR;
        str = s;
    }
    Property(boolean b) {
        type = PropType.BOOL;
        ival = b ? 1 : 0;
    }
    
    private PropType type;
    private String str;
    private long ival;
    private double[] vec;
    
    
    public boolean getBool() {
        switch (type) {
            case INT:
            case BOOL:
                return ival != 0;
            case STR:
                if (str==null) return false;
                if (str.equalsIgnoreCase("false")) return false;
                return str.length()>0;
            case REAL:
                return vec!=null && vec[0]!=0;
            case VEC:
                //Check other vector elements XXX
                return vec!=null && vec[0]!=0; 
        }
        return false;
    }
    
    public long getInt() {
        switch (type) {
            case INT:
            case BOOL:
                return ival;
            case STR:
                if (str==null) return 0;
                try {
                    return Long.parseLong(str);
                } catch (Exception x) {
                    return 0;
                }
            case REAL:
                if (vec==null) return 0;
                return Math.round(vec[0]);
            case VEC:
                // XXX return magnitude of vector
                if (vec==null) return 0;
                return Math.round(vec[0]);
        }
        return 0;
    }

    public double getReal() {
        switch (type) {
            case INT:
            case BOOL:
                return ival;
            case STR:
                if (str==null) return 0;
                try {
                    return Double.parseDouble(str);
                } catch (Exception x) {
                    return 0;
                }
            case REAL:
                if (vec==null) return 0;
                return vec[0];
            case VEC:
                // XXX return magnitude of vector
                if (vec==null) return 0;
                return vec[0];
        }
        return 0;
    }
    
    public String getStr() {
        switch (type) {
            case INT:
                return Long.toString(ival);
            case BOOL:
                return (ival!=0) ? "true" : "false";
            case STR:
                if (str==null) return "";
                return str;
            case REAL:
                if (vec==null) return "";
                return Double.toString(vec[0]);
            case VEC:
                // XXX <tuple>
                if (vec==null) return "";
                return Double.toString(vec[0]);
        }
        return "";
    }

    public double[] getVec() {
        switch (type) {
            case INT:
            case BOOL:
                if (vec==null || vec.length!=1) vec = new double[1];
                vec[0] = ival;
                return vec;
            case STR:
                if (vec==null || vec.length!=1) vec = new double[1];
                try {
                    vec[0] = Double.parseDouble(str);
                } catch (Exception x) {
                    vec[0] = 0;
                }
                return vec;
            case REAL:
            case VEC:
                if (vec==null) vec = new double[1];
                return vec;
        }
        if (vec==null) vec = new double[1];
        return vec;
    }

    public double getVec(int ix) {
        switch (type) {
            case INT:
            case BOOL:
                return ival;
            case STR:
                if (str==null) return 0;
                return Double.parseDouble(str);
            case REAL:
                if (vec==null) return 0;
                return vec[0];
            case VEC:
                if (vec==null) return 0;
                if (ix >= vec.length || ix < 0) return 0;
                return vec[ix];
        }
        return 0;
    }
    
    public void setStr(String s) {
        type = PropType.STR;
        str = s;
        vec = null;
    }
    
    public void setInt(long val) {
        type = PropType.INT;
        ival = val;
        str = null;
        vec = null;
    }
    
    public void setReal(double val) {
        type = PropType.REAL;
        str = null;
        if (vec==null || vec.length!=1) vec = new double[1];
        vec[0] = val;
    }
    
    public void setBool(boolean b) {
        type = PropType.BOOL;
        vec = null;
        str = null;
        ival = b ? 1 : 0;
    }
    
    public void setVec(double[] v) {
        type = PropType.VEC;
        str = null;
        vec = new double[v.length];
        System.arraycopy(v, 0, vec, 0, v.length);
    }
    
    public void setVec(double v, int ix) {
        if (ix < 0) return;
        if (vec==null) {
            vec = new double[ix+1];
        } else if (ix >= vec.length) {
            double [] nv = new double[ix+1];
            System.arraycopy(vec, 0, nv, 0, vec.length);
            vec = nv;
        }
        vec[ix] = v;
    }
    
    public static void addToVec(double[] dst, double[] src) {
        for (int i=0; i<dst.length && i<src.length; i++) {
            dst[i] += src[i];
        }
    }
    
    static public Property zero_vec, zero_int, zero_real, empty_str, null_prop;
    static {
        zero_vec = new Property();
        zero_vec.vec = new double[6];
        zero_vec.type = PropType.VEC;
        
        zero_int = new Property();
        zero_int.type = PropType.INT;

        zero_real = new Property();
        zero_real.type = PropType.REAL;
        zero_real.vec = zero_vec.vec;
        
        empty_str = new Property();
        empty_str.type = PropType.STR;
        empty_str.str = "";
        
        null_prop = new Property();
        null_prop.type = PropType.NULL;
    }
    
    static public Property add(Property a, Property b) {
        if (a.type == PropType.STR || b.type == PropType.STR) {
            String sa = a.getStr();
            String sb = b.getStr();
            return new Property(sa + sb);
        }
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                vc[i] = va[i % va.length] + vb[i % vb.length];
            }
            return new Property(vc);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra + rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        System.out.println("a=" + ia + " b=" + ib);
        return new Property(ia + ib);
    }
    
    static public Property sub(Property a, Property b) {
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                vc[i] = va[i % va.length] - vb[i % vb.length];
            }
            return new Property(vc);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra - rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia - ib);
    }
    
    static public Property mul(Property a, Property b) {
        // XXX multiply string by int should replicate string
        // XXX need vector products?
        
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                vc[i] = va[i % va.length] * vb[i % vb.length];
            }
            return new Property(vc);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra * rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia * ib);
    }

    static public Property div(Property a, Property b) {
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                vc[i] = va[i % va.length] / vb[i % vb.length];
            }
            return new Property(vc);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra / rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia / ib);
    }

    static public Property mod(Property a, Property b) {
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                vc[i] = va[i % va.length] % vb[i % vb.length];
            }
            return new Property(vc);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra % rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia % ib);
    }
    
    static public Property eq(Property a, Property b) {
        if (a.type == PropType.STR || b.type == PropType.STR) {
            String sa = a.getStr();
            String sb = b.getStr();
            return new Property(sa.equals(sb));
        }
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                if (va[i % va.length] != vb[i % vb.length]) return new Property(false);
            }
            return new Property(true);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra == rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia == ib);
    }
    
    static public Property gt(Property a, Property b) {
        if (a.type == PropType.STR || b.type == PropType.STR) {
            String sa = a.getStr();
            String sb = b.getStr();
            return new Property(sa.compareTo(sb)>0);
        }
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                if (va[i % va.length] <= vb[i % vb.length]) return new Property(false);
            }
            return new Property(true);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra > rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia > ib);
    }
    
    static public Property lt(Property a, Property b) {
        if (a.type == PropType.STR || b.type == PropType.STR) {
            String sa = a.getStr();
            String sb = b.getStr();
            return new Property(sa.compareTo(sb)<0);
        }
        if (a.type == PropType.VEC || b.type == PropType.VEC) {
            double[] va = a.getVec();
            double[] vb = b.getVec();
            double[] vc = new double[Integer.max(va.length, vb.length)];
            for (int i=0; i<vc.length; i++) {
                if (va[i % va.length] >= vb[i % vb.length]) return new Property(false);
            }
            return new Property(true);
        }
        if (a.type == PropType.REAL || b.type == PropType.REAL) {
            double ra = a.getReal();
            double rb = b.getReal();
            return new Property(ra < rb);
        }
        long ia = a.getInt();
        long ib = b.getInt();
        return new Property(ia < ib);
    }
}
