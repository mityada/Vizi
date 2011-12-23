package ru.ifmo.vizi.find_max;

import ru.ifmo.vizi.base.auto.*;
import java.util.Locale;

public final class FindMaximum extends BaseAutomataWithListener {
    /**
      * ������ ������.
      */
    public final Data d = new Data();

    /**
      * ����������� ��� �����
      */
    public FindMaximum(Locale locale) {
        super("ru.ifmo.vizi.find_max.Comments", locale); 
        init(new Main(), d); 
    }

    /**
      * ������.
      */
    public final class Data {
        /**
          * ������ ��� ������.
          */
        public int[] a = new int[]{1, 2, 3, 1, 3, 5, 6};

        /**
          * ��������� �������.
          */
        public FindMaximumVisualizer visualizer = null;

        /**
          * ������� ��������.
          */
        public int max = 0;

        /**
          * ���������� ����� (��������� Main).
          */
        public int Main_i;

        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("max = ").append(d.max).append("\n");
            s.append("i = ").append(d.Main_i).append("\n");
            return s.toString();
        }
    }

    /**
      * ���� �������� � �������.
      */
    private final class Main extends BaseAutomata implements Automata {
        /**
          * ��������� ��������� ��������.
          */
        private final int START_STATE = 0;

        /**
          * �������� ��������� ��������.
          */
        private final int END_STATE = 8;

        /**
          * �����������.
          */
        public Main() {
            super( 
                "Main", 
                0, // ����� ���������� ��������� 
                8, // ����� ��������� ��������� 
                new String[]{ 
                    "��������� ���������",  
                    "�������������", 
                    "������ �����", 
                    "����", 
                    "�������", 
                    "������� (���������)", 
                    "���������� ���������", 
                    "���������", 
                    "�������� ���������" 
                }, new int[]{ 
                    Integer.MAX_VALUE, // ��������� ���������,  
                    0, // ������������� 
                    -1, // ������ ����� 
                    -1, // ���� 
                    0, // ������� 
                    -1, // ������� (���������) 
                    0, // ���������� ��������� 
                    -1, // ��������� 
                    Integer.MAX_VALUE, // �������� ��������� 
                } 
            ); 
        }

        /**
          * ������� ���� ��� �������� � �����.
          */
        protected void doStepForward(int level) {
            // ������� � ��������� ���������
            switch (state) {
                case START_STATE: { // ��������� ���������
                    state = 1; // �������������
                    break;
                }
                case 1: { // �������������
                    state = 2; // ������ �����
                    break;
                }
                case 2: { // ������ �����
                    stack.pushBoolean(false); 
                    state = 3; // ����
                    break;
                }
                case 3: { // ����
                    if (d.Main_i < d.a.length) {
                        state = 4; // �������
                    } else {
                        state = END_STATE; 
                    }
                    break;
                }
                case 4: { // �������
                    if (d.max < d.a[d.Main_i]) {
                        state = 6; // ���������� ���������
                    } else {
                        stack.pushBoolean(false); 
                        state = 5; // ������� (���������)
                    }
                    break;
                }
                case 5: { // ������� (���������)
                    state = 7; // ���������
                    break;
                }
                case 6: { // ���������� ���������
                    stack.pushBoolean(true); 
                    state = 5; // ������� (���������)
                    break;
                }
                case 7: { // ���������
                    stack.pushBoolean(true); 
                    state = 3; // ����
                    break;
                }
            }

            // �������� � ������� ���������
            switch (state) {
                case 1: { // �������������
                    d.max = 0;
                    break;
                }
                case 2: { // ������ �����
                    d.Main_i = 0;
                    break;
                }
                case 3: { // ����
                    break;
                }
                case 4: { // �������
                    break;
                }
                case 5: { // ������� (���������)
                    break;
                }
                case 6: { // ���������� ���������
                    // @!max
                    stack.pushInteger(d.max);
                    d.max = d.a[d.Main_i];
                    break;
                }
                case 7: { // ���������
                    d.Main_i++;
                    break;
                }
            }
        }

        /**
          * ������� ���� ��� �������� �����.
          */
        protected void doStepBackward(int level) {
            // ��������� �������� � ������� ���������
            switch (state) {
                case 1: { // �������������
                    break;
                }
                case 2: { // ������ �����
                    break;
                }
                case 3: { // ����
                    break;
                }
                case 4: { // �������
                    break;
                }
                case 5: { // ������� (���������)
                    break;
                }
                case 6: { // ���������� ���������
                    // @?max
                    d.max = stack.popInteger();
                    break;
                }
                case 7: { // ���������
                    d.Main_i--;
                    break;
                }
            }

            // ������� � ���������� ���������
            switch (state) {
                case 1: { // �������������
                    state = START_STATE; 
                    break;
                }
                case 2: { // ������ �����
                    state = 1; // �������������
                    break;
                }
                case 3: { // ����
                    if (stack.popBoolean()) {
                        state = 7; // ���������
                    } else {
                        state = 2; // ������ �����
                    }
                    break;
                }
                case 4: { // �������
                    state = 3; // ����
                    break;
                }
                case 5: { // ������� (���������)
                    if (stack.popBoolean()) {
                        state = 6; // ���������� ���������
                    } else {
                        state = 4; // �������
                    }
                    break;
                }
                case 6: { // ���������� ���������
                    state = 4; // �������
                    break;
                }
                case 7: { // ���������
                    state = 5; // ������� (���������)
                    break;
                }
                case END_STATE: { // ��������� ���������
                    state = 3; // ����
                    break;
                }
            }
        }

        /**
          * ����������� � �������� ���������
          */
        public String getComment() {
            String comment = ""; 
            Object[] args = null; 
            // ����� �����������
            switch (state) {
                case START_STATE: { // ��������� ���������
                    comment = FindMaximum.this.getComment("Main.START_STATE"); 
                    break;
                }
                case 1: { // �������������
                    comment = FindMaximum.this.getComment("Main.Initialization"); 
                    break;
                }
                case 4: { // �������
                    if (d.max < d.a[d.Main_i]) {
                        comment = FindMaximum.this.getComment("Main.Cond.true"); 
                    } else {
                        comment = FindMaximum.this.getComment("Main.Cond.false"); 
                    }
                    args = new Object[]{new Integer(d.a[d.Main_i]), new Integer(d.max)}; 
                    break;
                }
                case 6: { // ���������� ���������
                    comment = FindMaximum.this.getComment("Main.newMax"); 
                    break;
                }
                case END_STATE: { // �������� ���������
                    comment = FindMaximum.this.getComment("Main.END_STATE"); 
                    args = new Object[]{new Integer(d.max)}; 
                    break;
                }
            }

            return java.text.MessageFormat.format(comment, args); 
        }

        /**
          * ��������� �������� �� ��������� ���������
          */
        public void drawState() {
            switch (state) {
                case START_STATE: { // ��������� ���������
                    d.visualizer.updateArray(0, 0);
                    break;
                }
                case 1: { // �������������
                    d.visualizer.updateArray(0, 0);
                    break;
                }
                case 4: { // �������
                    d.visualizer.updateArray(d.Main_i, 1);
                    break;
                }
                case 6: { // ���������� ���������
                    d.visualizer.updateArray(d.Main_i, 2);
                    break;
                }
                case END_STATE: { // �������� ���������
                    d.visualizer.updateArray(0, 0);
                    break;
                }
            }
        }
    }
}
