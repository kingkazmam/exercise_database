//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:57 AM
//

package DIaLOGIKa.b2xtranslator.DocFileFormat;

import DIaLOGIKa.b2xtranslator.CommonTranslatorLib.IMapping;
import DIaLOGIKa.b2xtranslator.CommonTranslatorLib.IVisitable;
import DIaLOGIKa.b2xtranslator.DocFileFormat.BorderCode;
import DIaLOGIKa.b2xtranslator.DocFileFormat.ByteParseException;
import DIaLOGIKa.b2xtranslator.DocFileFormat.Global;

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
public class BorderCode   implements IVisitable
{
    public enum BorderType
    {
        none,
        single,
        thick,
        Double,
        unused,
        hairline,
        dotted,
        dashed,
        dotDash,
        dotDotDash,
        triple,
        thinThickSmallGap,
        thickThinSmallGap,
        thinThickThinSmallGap,
        thinThickMediumGap,
        thickThinMediumGap,
        thinThickThinMediumGap,
        thinThickLargeGap,
        thickThinLargeGap,
        thinThickThinLargeGap,
        wave,
        doubleWave,
        dashSmallGap,
        dashDotStroked,
        threeDEmboss,
        threeDEngrave
    }
    /**
    * 24-bit border color
    */
    public int cv;
    /**
    * Width of a single line in 1/8pt, max of 32pt
    */
    public byte dptLineWidth;
    /**
    * Border type code:
    * 0 none
    * 1 single
    * 2 thick
    * 3 double
    * 5 hairline
    * 6 dot
    * 7 dash large gap
    * 8 dot dash
    * 9 dot dot dash
    * 10 triple
    * 11 thin-thick small gap
    * 12 tick-thin small gap
    * 13 thin-thick-thin small gap
    * 14 thin-thick medium gap
    * 15 thick-thin medium gap
    * 16 thin-thick-thin medium gap
    * 17 thin-thick large gap
    * 18 thick-thin large gap
    * 19 thin-thick-thin large gap
    * 20 wave
    * 21 double wave
    * 22 dash small gap
    * 23 dash dot stroked
    * 24 emboss 3D
    * 25 engrave 3D
    */
    public byte brcType;
    /**
    * The color of the Border.
    * Unused if cv is set.
    */
    public DIaLOGIKa.b2xtranslator.DocFileFormat.Global.ColorIdentifier ico = DIaLOGIKa.b2xtranslator.DocFileFormat.Global.ColorIdentifier.auto;
    /**
    * Width of space to maintain between border and text within border
    */
    public int dptSpace;
    /**
    * When true, border is drawn with shadow. Must be false when BRC is substructure of the TC
    */
    public boolean fShadow;
    /**
    * When true, don't reverse the border
    */
    public boolean fFrame;
    /**
    * It's a nil BRC, bytes are FFFF.
    */
    public boolean fNil;
    /**
    * Creates a new BorderCode with default values
    */
    public BorderCode() throws Exception {
    }

    /**
    * Parses the byte for a BRC
    * 
    *  @param bytes
    */
    public BorderCode(byte[] bytes) throws Exception {
        if (DIaLOGIKa.b2xtranslator.Tools.Utils.arraySum(bytes) == bytes.length * 255)
        {
            this.fNil = true;
        }
        else if (bytes.length == 8)
        {
            //it's a border code of Word 2000/2003
            this.cv = System.BitConverter.ToInt32(bytes, 0);
            this.ico = DIaLOGIKa.b2xtranslator.DocFileFormat.Global.ColorIdentifier.auto;
            this.dptLineWidth = bytes[4];
            this.brcType = bytes[5];
            short val = System.BitConverter.ToInt16(bytes, 6);
            this.dptSpace = val & 0x001F;
            //not sure if this is correct, the values from the spec are definitly wrong:
            this.fShadow = DIaLOGIKa.b2xtranslator.Tools.Utils.BitmaskToBool(val, 0x20);
            this.fFrame = DIaLOGIKa.b2xtranslator.Tools.Utils.BitmaskToBool(val, 0x40);
        }
        else if (bytes.length == 4)
        {
            //it's a border code of Word 97
            UInt16 val = System.BitConverter.ToUInt16(bytes, 0);
            this.dptLineWidth = (byte)(val & 0x00FF);
            this.brcType = (byte)((val & 0xFF00) >> 8);
            val = System.BitConverter.ToUInt16(bytes, 2);
            this.ico = (DIaLOGIKa.b2xtranslator.DocFileFormat.Global.ColorIdentifier)(val & 0x00FF);
            this.dptSpace = (val & 0x1F00) >> 8;
        }
        else
        {
            throw new ByteParseException("Cannot parse the struct BRC, the length of the struct doesn't match");
        }   
    }

    public <T>void convert(T mapping) throws Exception {
        ((IMapping<BorderCode>)mapping).apply(this);
    }

}


