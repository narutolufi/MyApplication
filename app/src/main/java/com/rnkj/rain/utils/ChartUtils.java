package com.rnkj.rain.utils;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ChartUtils {

    //依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
    public static Position CalcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle,float gap){
        //将角度转换为弧度
        float arcAngle = (float) (Math.PI * cirAngle / 180.0);
        if (cirAngle < 90){
            Position position = new Position();
            position.posX = cirX + (float)(Math.cos(arcAngle)) * radius;
            position.posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            if(gap > 0){
                position.posY -=  gap;
            }
            return position;
        }else if (cirAngle == 90){
            Position position = new Position();
            position.posX = cirX;
            position.posY = cirY + radius;
            if(gap > 0){
                position.posY += gap;
            }
            return position;
        }else if (cirAngle > 90 && cirAngle < 180){
            arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
            Position position = new Position();
            position.posX = cirX - (float)(Math.cos(arcAngle)) * radius;
            position.posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            if(gap > 0){
                position.posX -= 2*gap;
            }
            return position;
        }else if (cirAngle == 180){
            Position position = new Position();
            position.posX = cirX - radius;
            position.posY = cirY;
            if(gap > 0){
                position.posX = position.posX - 1.5f * gap;
            }
            return position;
        }else if (cirAngle > 180 && cirAngle < 270){
            Position position = new Position();
            arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
            position.posX = cirX - (float)(Math.cos(arcAngle)) * radius;
            position.posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            if(gap > 0){
                position.posX -= gap;
                position.posY -= gap;
            }
            return position;
        }else if (cirAngle == 270){
            Position position = new Position();
            position.posX = cirX;
            position.posY = cirY - radius;
            if(gap > 0){
                position.posY = position.posY - 0.5f * gap;
            }
            return position;
        }else{
            Position position = new Position();
            arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
            position.posX = cirX + (float)(Math.cos(arcAngle)) * radius;
            position.posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            return position;
        }
    }

    public static float CalcArcAngle(float cirAngle){
        if (cirAngle < 90){
            return cirAngle + 270;
        }else if (cirAngle == 90){
            return 0;
        }else if (cirAngle > 90 && cirAngle < 180){
            return cirAngle - 90;
        }else if (cirAngle == 180){
            return 90;
        }else if (cirAngle > 180 && cirAngle < 270){
            return cirAngle - 90;
        }else if (cirAngle == 270){
            return 180;
        }else if(cirAngle > 270 && cirAngle < 360){
            return cirAngle - 90;
        }else{
            return 270;
        }
    }

    public static double[] rotateVec(float px, float py, double ang, boolean isChLen, double newLen)
    {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }


    public static class Position{
        public float posX;
        public float posY;
        public Position(){
        }
    }
}
