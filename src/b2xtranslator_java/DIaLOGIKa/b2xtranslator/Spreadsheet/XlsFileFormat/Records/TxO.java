//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:50 AM
//

package DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records;

import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecord;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecordAttribute;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.RecordType;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Structures.ControlInfo;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Structures.ObjFmla;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Structures.TxORuns;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Structures.XLUnicodeStringNoCch;
import DIaLOGIKa.b2xtranslator.StructuredStorage.Reader.IStreamReader;

/*
 * Copyright (c) 2009, DIaLOGIKa
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright 
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the 
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the names of copyright holders, nor the names of its contributors 
 *       may be used to endorse or promote products derived from this software 
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
/**
* This record specifies the text in a text box or a form control.
*/
public class TxO  extends BiffRecord 
{
    public static final RecordType ID = RecordType.TxO;
    public enum HorizontalAlignment
    {
        __dummyEnum__0,
        Left,
        Centered,
        Right,
        Justify,
        __dummyEnum__1,
        __dummyEnum__2,
        JustifyDistributed
    }
    public enum VerticalAlignment
    {
        __dummyEnum__0,
        Top,
        Middle,
        Bottom,
        Justify,
        __dummyEnum__1,
        __dummyEnum__2,
        JustifyDistributed
    }
    public enum TextRotation
    {
        Custom,
        Stacked,
        CounterClockwise,
        Clockwise
    }
    /**
    * Specifies the horizontal alignment.
    */
    public HorizontalAlignment hAlignment = HorizontalAlignment.Left;
    /**
    * Specifies the vertical alignment.
    */
    public VerticalAlignment vAlignment = VerticalAlignment.Top;
    /**
    * Specifies the orientation of the text within the object boundary.
    */
    public TextRotation rot = TextRotation.Custom;
    /**
    * An optional ControlInfo that specifies the properties for some form controls.
    * 
    * The field MUST exist if and only if the value of cmo.ot in the preceding Obj record is 0, 5, 7, 11, 12 or 14.
    */
    public ControlInfo controlInfo;
    /**
    * An unsigned integer that specifies the number of characters in the text string
    * contained in the Continue records immediately following this record. 
    * MUST be less than or equal to 255.
    */
    public UInt16 cchText = new UInt16();
    /**
    * An unsigned integer that specifies the number of bytes of formatting run information in the
    * TxORuns structure contained in the Continue records following this record.
    * If cchText is 0, this value MUST be 0.
    * Otherwise the value MUST be greater than or equal to 16 and MUST be a multiple of 8.
    */
    public UInt16 cbRuns = new UInt16();
    /**
    * A FontIndex that specifies the font when cchText is 0.
    */
    public UInt16 ifntEmpty = new UInt16();
    /**
    * An ObjFmla that specifies the parsed expression of the formula for the text.
    */
    public ObjFmla fmla;
    /**
    * Text String Specification: The first set of Continue records specifies the text string.
    * Each of these Continue record contains an XLUnicodeStringNoCch that specifies
    * part of the string. The total number of characters in all XLUnicodeStringNoCch MUST be cchText.
    */
    public XLUnicodeStringNoCch text;
    /**
    * Formatting Run Specification: The second set of Continue records specifies formatting runs.
    * These Continue records contain a TxORuns structure. If the size of the TxORuns structure
    * is longer than 8,224 bytes, it is split across multiple Continue records.
    */
    public TxORuns runs;
    public TxO(IStreamReader reader, RecordType id, UInt16 length) throws Exception {
        super(reader, id, length);
        // assert that the correct record type is instantiated
        Debug.Assert(this.Id == ID);
        long startPosition = this.Reader.BaseStream.Position;
        for (int noOfTries = 1;noOfTries < 3;noOfTries++)
        {
            // NOTE: controlInfo is an option field that exists if and only if the value of
            //   cmo.ot in the preceding Obj record is 0, 5, 7, 11, 12 or 14.
            //   However, the current parser implementation does not allow us to see the preceding
            //   record(s) witout an enormous recactoring. Therefore we're a little bit hacky here
            //   and try to read the record first without the optional field. If we didn't succeed
            //   we start a second try, this time including the optional field.
            //
            // initialize class members from stream
            UInt16 flags = reader.readUInt16();
            this.hAlignment = (HorizontalAlignment)DIaLOGIKa.b2xtranslator.Tools.Utils.BitmaskToInt(flags, 0xE);
            this.vAlignment = (VerticalAlignment)DIaLOGIKa.b2xtranslator.Tools.Utils.BitmaskToInt(flags, 0x70);
            this.rot = (TextRotation)reader.readUInt16();
            reader.readBytes(6);
            // reserved
            // read optional field controlInfo on second try
            if (noOfTries == 2)
            {
                this.controlInfo = new ControlInfo(reader);
            }
             
            this.cchText = reader.readUInt16();
            this.cbRuns = reader.readUInt16();
            this.ifntEmpty = reader.readUInt16();
            this.fmla = new ObjFmla(reader);
            if (this.Offset + this.Length == this.Reader.BaseStream.Position)
            {
                break;
            }
            else if (noOfTries == 1)
            {
                // re-read record, this time including the optional controlInfo field
                this.Reader.BaseStream.Position = startPosition;
            }
              
        }
        // assert that the correct number of bytes has been read from the stream
        Debug.Assert(this.Offset + this.Length == this.Reader.BaseStream.Position);
        // NOTE: If the field cchText is not zero, this record doesn�t fully specify the text.
        //  The rest of the data that MUST be specified is the text string and the formatting runs
        //  information.
        //
        if (this.cchText > 0 && BiffRecord.getNextRecordType(reader) == RecordType.Continue)
        {
            // skip record header
            reader.readUInt16();
            reader.readUInt16();
            this.text = new XLUnicodeStringNoCch(reader,this.cchText);
            if (BiffRecord.getNextRecordType(reader) == RecordType.Continue)
            {
                // skip record header
                reader.readUInt16();
                reader.readUInt16();
                this.runs = new TxORuns(reader,this.cbRuns);
            }
             
        }
         
    }

}


