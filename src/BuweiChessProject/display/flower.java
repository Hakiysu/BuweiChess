package BuweiChessProject.display;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class flower extends Frame{
    //背景
    int[] b1x ={0,500,500,0};
    int[] b1y ={0,0,500,500};
    Thread thread ;
    int count=8;//烟花个数 8
    public flower() {

        setLayout(new FlowLayout());
        setTitle("烟花特效");
        setSize(500, 500);
        setVisible(true);

    }
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);                   // 描画颜色
        g.fillPolygon(b1x, b1y, 4);              // 描画

        while(true){
            g.setColor(Color.BLACK);                   // 描画颜色
            g.fillPolygon(b1x, b1y, 4);              // 描画
            int flowerx=(int) (Math.random() * 500);//烟花x坐标
            int flowery=100+(int) (Math.random() * 300); //烟花最终y坐标
            int movey=500;//烟花弹移动变量
            while(movey>=flowery)//当
            { g.setColor(Color.BLACK);                   // 描画颜色
                g.fillPolygon(b1x, b1y, 4);              // 描画
                g.setColor(Color.WHITE);                   // 烟花弹颜色白色
                g.fillOval(flowerx,movey,1200/flowery ,1200/flowery);//烟花弹大小
                try {
                    Thread.sleep(10);//延时

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //用黑色抹去烟花弹踪迹
                g.setColor(Color.BLACK);                   // 画画颜色
                g.fillOval(flowerx,movey+1200/flowery,1200/flowery ,1200/flowery);

                movey-=1200/flowery;//向上移动幅度
            }
            g.setColor(Color.WHITE);                   // 烟花弹颜色白色
            g.fillOval(flowerx,flowery,1600/flowery,1600/flowery);//烟花弹大小
            try {
                Thread.sleep(30);//延时

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //抹去烟花弹
            g.setColor(Color.BLACK);                   // 描画颜色
            g.fillPolygon(b1x, b1y, 4);              // 描画
            //烟花炸出以烟花弹终点坐标为圆心的40个小烟花
            for(int i=0;i<10;i++){//

                int r=3+(int) (Math.random() * 400/flowery);//花骨朵半径

                //右下象限
                Color c1 = new Color((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));//花骨朵颜色
                g.setColor(c1);
                g.fillOval(flowerx+(int) (Math.random() * r*50),flowery+(int) (Math.random() * r*50),r , r);

                //左上象限
                Color c2 = new Color((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));//花骨朵颜色
                g.setColor(c2);
                g.fillOval(flowerx-(int) (Math.random() * r*50),flowery-(int) (Math.random() * r*50),r , r);
                //左下象限
                Color c3 = new Color((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));//花骨朵颜色
                g.setColor(c3);
                g.fillOval(flowerx-(int) (Math.random() * r*50),flowery+(int) (Math.random() * r*50),r , r);
                //右上象限
                Color c4 = new Color((int) (Math.random() * 255),(int) (Math.random()  * 255),(int) (Math.random() * 255));//花骨朵颜色
                g.setColor(c4);
                g.fillOval(flowerx+(int) (Math.random()  * r*50),flowery-(int) (Math.random()  * r*50),r , r);
            }
            try {
                Thread.sleep(100);//延时

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count--;//烟花减1
            if(count<=0)break;//放完退出循环
        }
        System.exit(0); //退出窗体
    }




    class WinAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent we) {
            System.exit(0);
        }
    }

    public static void func()  {
        flower f = new flower();
    }
}