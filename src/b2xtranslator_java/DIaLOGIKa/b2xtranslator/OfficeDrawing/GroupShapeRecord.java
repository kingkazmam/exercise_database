//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:47:32 AM
//

package DIaLOGIKa.b2xtranslator.OfficeDrawing;

import DIaLOGIKa.b2xtranslator.OfficeDrawing.OfficeRecordAttribute;
import DIaLOGIKa.b2xtranslator.OfficeDrawing.Record;

/*
 * Copyright (c) 2008, DIaLOGIKa
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of DIaLOGIKa nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DIaLOGIKa ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DIaLOGIKa BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class GroupShapeRecord  extends Record 
{
    /**
    * 
    */
    public Rectangle rcgBounds = new Rectangle();
    public GroupShapeRecord(BinaryReader _reader, uint size, uint typeCode, uint version, uint instance) throws Exception {
        super(_reader, size, typeCode, version, instance);
        //read the rectangle (16 bytes)
        int left = Math.Max(0, this.Reader.ReadInt32());
        int top = Math.Max(0, this.Reader.ReadInt32());
        int right = Math.Max(0, this.Reader.ReadInt32());
        int bottom = Math.Max(0, this.Reader.ReadInt32());
        this.rcgBounds = new Rectangle(new Point(left, top), new Size(right - left, bottom - top));
    }

}


