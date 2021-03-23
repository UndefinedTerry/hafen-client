/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.util.*;
import haven.render.*;

public class ResDrawable extends Drawable {
    public final Indir<Resource> res;
    public Sprite spr;
    MessageBuf sdt;
    // private double delay = 0; XXXRENDER

    public ResDrawable(Gob gob, Indir<Resource> res, Message sdt) {
	super(gob);
	this.res = res;
	this.sdt = new MessageBuf(sdt);
	//spr = Sprite.create(gob, res.get(), this.sdt.clone());
	init();
    }
    
    public void init() {
	Resource res = this.res.get();
	if (this.gob.type == null)
	    gob.determineType(res.name);
    
	MessageBuf sdtCopy = this.sdt.clone();
	if (CFG.UND_MINIFY_TREES.get() && (gob.type == Gob.Type.TREE || gob.type == Gob.Type.BUSH) && !sdtCopy.eom()) {
	    byte[] args = new byte[2];
	    args[0] = (byte)sdtCopy.uint8();
	    int fscale = 25;
	    if (!sdtCopy.eom()) {
		fscale = sdtCopy.uint8();
		if (fscale > 25)
		    fscale = 25;
	    
	    }
	    args[1] = (byte)fscale;
	    sdtCopy = new MessageBuf(args);
	}
 
	spr = Sprite.create(gob, res, sdtCopy);
    }

    public ResDrawable(Gob gob, Resource res) {
	this(gob, res.indir(), MessageBuf.nil);
    }

    public void ctick(double dt) {
	spr.tick(dt);
    }

    public void gtick(Render g) {
	spr.gtick(g);
    }

    public void added(RenderTree.Slot slot) {
	slot.add(spr);
	super.added(slot);
    }

    public void dispose() {
	if(spr != null)
	    spr.dispose();
    }

    public Resource getres() {
	return(res.get());
    }

    @Override
    public Indir<Resource> getires() {
	return res;
    }
    
    public Skeleton.Pose getpose() {
	return(Skeleton.getpose(spr));
    }
}
