package io.anuke.mindustry.entities.bullet;

import io.anuke.arc.graphics.*;
import io.anuke.arc.graphics.g2d.*;
import io.anuke.arc.math.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.entities.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.graphics.*;

public class LaserBulletType extends BulletType{
    protected Color[] colors = {Pal.lancerLaser.cpy().mul(1f, 1f, 1f, 0.4f), Pal.lancerLaser, Color.white};
    protected float length = 160f;
    protected float width = 15f;
    protected float lengthFalloff = 0.5f;
    protected float sideLength = 29f, sideWidth = 0.7f;
    protected float sideAngle = 90f;

    public LaserBulletType(float damage){
        super(0.01f, damage);

        keepVelocity = false;
        hitEffect = Fx.hitLancer;
        despawnEffect = Fx.none;
        shootEffect = Fx.hitLancer;
        smokeEffect = Fx.lancerLaserShootSmoke;
        hitSize = 4;
        lifetime = 16f;
        pierce = true;
    }

    @Override
    public float range(){
        return length;
    }

    @Override
    public void init(Bullet b){
        Damage.collideLine(b, b.getTeam(), hitEffect, b.x, b.y, b.rot(), length);
    }

    @Override
    public void draw(Bullet b){
        float f = Mathf.curve(b.fin(), 0f, 0.2f);
        float baseLen = length * f;
        float cwidth = width;
        float compound = 1f;

        Lines.lineAngle(b.x, b.y, b.rot(), baseLen);
        Lines.precise(true);
        for(Color color : colors){
            Draw.color(color);
            Lines.stroke((cwidth *= lengthFalloff) * b.fout());
            Lines.lineAngle(b.x, b.y, b.rot(), baseLen, CapStyle.none);
            Tmp.v1.trns(b.rot(), baseLen);
            Drawf.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, Lines.getStroke() * 1.22f, cwidth * 2f + width / 2f, b.rot());

            Fill.circle(b.x, b.y, 1f * cwidth * b.fout());
            for(int i : Mathf.signs){
                Drawf.tri(b.x, b.y, sideWidth * b.fout() * cwidth, sideLength * compound, b.rot() + sideAngle * i);
            }

            compound *= lengthFalloff;
        }
        Lines.precise(false);
        Draw.reset();
    }
}
