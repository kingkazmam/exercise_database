//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:42 AM
//

package DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records;

import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecord;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecordAttribute;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.RecordType;
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
public class CrtLine  extends BiffRecord 
{
    public static final RecordType ID = RecordType.CrtLine;
    public enum LineType
    {
        /**
        * Drop lines below the data points of line, area, and stock chart groups.
        */
        DropLines,
        /**
        * High-Low lines around the data points of line and stock chart groups.
        */
        HighLowLines,
        /**
        * Series lines connecting data points of stacked column and bar chart groups,
        * and the primary pie to the secondary bar/pie of bar of pie and pie of pie chart groups.
        */
        SeriesLines,
        /**
        * Leader lines with non-default formatting connecting data labels to the
        * data point of pie and pie of pie chart groups.
        */
        LeaderLines
    }
    /**
    * An unsigned integer that specifies the type of line that is present on the chart group.
    * 
    * This field value MUST be unique among the other id field values in CrtLine records
    * in the current chart group. This field MUST be greater than the id field values in
    * preceding CrtLine records in the current chart group.
    * 
    * MUST be a value from the following table:
    * 
    * Value           Type of line
    * 0x0000          Drop lines below the data points of line, area, and stock chart groups.
    * 0x0001          High-Low lines around the data points of line and stock chart groups.
    * 0x0002          Series lines connecting data points of stacked column and bar chart groups,
    * and the primary pie to the secondary bar/pie of bar of pie and pie of pie chart groups.
    * 0x0003          Leader lines with non-default formatting connecting data labels to the
    * data point of pie and pie of pie chart groups.
    */
    public LineType lineId = LineType.DropLines;
    public CrtLine(IStreamReader reader, RecordType id, UInt16 length) throws Exception {
        super(reader, id, length);
        // assert that the correct record type is instantiated
        Debug.Assert(this.Id == ID);
        // initialize class members from stream
        this.lineId = (LineType)reader.readUInt16();
        // assert that the correct number of bytes has been read from the stream
        Debug.Assert(this.Offset + this.Length == this.Reader.BaseStream.Position);
    }

}


