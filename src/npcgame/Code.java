/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npcgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author millerti
 */
public class Code {
    static abstract class Element {
        abstract public void exec(Context con);
    }
    static class Int extends Element {
        long num;
        Int(long n) { 
//            System.out.println("INT: " + n);
            num = n; 
        }
        
        @Override
        public void exec(Context con) {
            con.stack.add(new Property(num));
        }
        
        @Override
        public String toString() {
            return Long.toString(num);
        }
    }
    
    static class Real extends Element {
        double num;
        Real(double n) { 
//            System.out.println("REAL: " + n);
            num = n; 
        }
        
        @Override
        public void exec(Context con) {
            con.stack.add(new Property(num));
        }
        
        @Override
        public String toString() {
            return Double.toString(num);
        }
    }
    
    static class Str extends Element {
        String str;
        Str(String s) { 
//            System.out.println("STR: " + s);
            str = s; 
        }
        
        @Override
        public void exec(Context con) {
            con.stack.add(new Property(str));
        }

        @Override
        public String toString() {
            return str;
        }
    }
    
    static class Oper extends Element {
        static String binary_ops_str = "+-%|&*/<>=!";
        static Set<Character> binary_ops = new HashSet<Character>();
        static {
            for (int i=0; i<binary_ops_str.length(); i++) {
                binary_ops.add(binary_ops_str.charAt(i));
            }
        }
        
        char op;
        Oper(char o) { 
//            System.out.println("OPER: " + o);
            op = o; 
        }
        
        public Property call(Context con, String name) {
            Property ret = BuiltinFuncs.call(name, con);
            if (ret != null) return ret;
            
            return con.av.execScript(name);
        }
        
        @Override
        public void exec(Context con) {
            Property a = Property.null_prop;
            Property b = Property.null_prop;
            Property c = Property.null_prop;

            if (binary_ops.contains(op)) {
                b = con.pop();
                a = con.pop();
            } else {
                a = con.pop();
            }
            
            switch (op) {
                case '+':
                    con.push(Property.add(a, b));
                    break;
                case '-':
                    con.push(Property.sub(a, b));
                    break;
                case '*':
                    con.push(Property.mul(a, b));
                    break;
                case '/':
                    con.push(Property.div(a, b));
                    break;
                case '%':
                    con.push(Property.div(a, b));
                    break;
                case '&':
                    con.push(new Property(a.getBool() & b.getBool()));
                    break;
                case '|':
                    con.push(new Property(a.getBool() | b.getBool()));
                    break;
                case '~':
                    con.push(new Property(!a.getBool()));
                    break;
                case '=':
                    con.push(Property.eq(a, b));
                    break;
                case '<':
                    con.push(Property.lt(a, b));
                    break;
                case '>':
                    con.push(Property.gt(a, b));
                    break;
                case '@':
                    con.push(con.av.findProp(con.pop().getStr()));
                    break;
                case '!':
                    con.av.setProp(b.getStr(), a);
                    break;
                case '$':
                    con.push(call(con, a.getStr()));
                    break;
            }
        }
                
        @Override
        public String toString() {
            return Character.toString(op);
        }
    }
    
    public static class Context {
        Avatar av;
        List<Property> stack = new ArrayList<Property>();
        
        Property pop() {
            int l = stack.size();
            if (l < 1) return Property.null_prop;
            l--;
            Property a = stack.remove(l);
            return a;
        }
        
        void push(Property c) {
            stack.add(c);
        }
    }
    
    List<Element> sequence = new ArrayList<Element>();
    
    static public Code parse(String line) {
        Code code = new Code();
        
        boolean string=false, esc=false, name=false, dot=false;
        boolean commit = false;
        char op = '\0';
        StringBuilder word = new StringBuilder();
        
        int i = 0;
        int l = line.length();
        while (i<=l) {
            op = '\0';
            commit = false;
            char c;
            if (i==l) {
                c = '\0';
            } else {
                c = line.charAt(i);
            }
            
            if (string) {
                if (esc) {
                    switch (c) {
                        case 'n':
                            word.append('\n');
                            break;
                        case 't':
                            word.append('\t');
                            break;
                        default:
                            word.append(c);
                    }
                    esc = false;
                } else {
                    switch (c) {
                        case '\\':
                            esc = true;
                            break;
                        case '\"':
                            code.sequence.add(new Str(word.toString()));
                            word.setLength(0);
                            string = false;
                            esc = false;
                            name = false;
                            dot = false;
                            break;
                    }
                }
            } else {
                switch (c) {
                    case ' ':
                    case '\t':
                    case '\n':
                    case '\0':
                        commit = true;
                        break;

                    case '\"':
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '&':
                    case '|':
                    case '~':
                    case '!':
                    case '=':
                    case '<':
                    case '>':
                    case '?':
                    case '@':
                    case '%':
                    case '$':
                        commit = true;
                        op = c;
                        break;
                        
                    default:
                        if (c == '.') {
                            dot = true;
                        } else
                        if (!Character.isDigit(c)) {
                            name = true;
                        }
                        word.append(c);
                        break;
                }
                
                if (commit) {
                    if (word.length() > 0) {
                        if (name) {
                            code.sequence.add(new Str(word.toString()));
                        } else if (dot) {
                            code.sequence.add(new Real(Double.parseDouble(word.toString())));
                        } else {
                            code.sequence.add(new Int(Long.parseLong(word.toString())));
                        }
                        word.setLength(0);
                    }
                    esc = false;
                    name = false;
                    dot = false;
                    string = false;
                }
                if (op == '\"') {
                    string = true;
                } else
                if (op != '\0') {
                    code.sequence.add(new Oper(c));
                }
            }
            
            i++;
        }
        
        return code;
    }
    
    public Property execute(Avatar av) {
        Context con = new Context();
        con.av = av;
        for (Element e : sequence) {
            System.out.println("Execute: " + e);
            e.exec(con);
        }
        return con.pop();
    }
}

