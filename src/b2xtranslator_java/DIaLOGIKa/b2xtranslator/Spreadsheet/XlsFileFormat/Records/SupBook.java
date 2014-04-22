//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:49 AM
//

package DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records;

import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecord;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.BiffRecordAttribute;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.ExcelHelperClass;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.RecordType;
import DIaLOGIKa.b2xtranslator.StructuredStorage.Reader.IStreamReader;

/*
 * Copyright (c) 2008, DIaLOGIKa
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
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
public class SupBook  extends BiffRecord 
{
    public static final RecordType ID = RecordType.SupBook;
    public UInt16 ctab = new UInt16();
    public UInt16 cch = new UInt16();
    public String virtpathstring;
    public String[] rgst;
    public boolean isvirtpath;
    public boolean isexternalworkbookreferencing;
    public boolean isselfreferencing;
    public boolean isaddinreferencing;
    public boolean isunusedsupportinglink;
    public SupBook(IStreamReader reader, RecordType id, UInt16 length) throws Exception {
        super(reader, id, length);
        // assert that the correct record type is instantiated
        Debug.Assert(this.Id == ID);
        this.ctab = this.Reader.ReadUInt16();
        this.cch = this.Reader.ReadUInt16();
        this.isselfreferencing = true;
        this.isaddinreferencing = false;
        this.isvirtpath = false;
        this.isexternalworkbookreferencing = false;
        this.isunusedsupportinglink = false;
        // Check cch
        if (cch == 0x0401)
        {
            this.isselfreferencing = true;
        }
        else if (cch == 0x3A01)
        {
            this.isaddinreferencing = true;
        }
        else //0x0001 to 0x00ff (inclusive)
        if (cch >= 0x0001 && cch <= 0x00ff)
        {
            this.isvirtpath = true;
        }
           
        if (this.isvirtpath)
        {
            this.virtpathstring = "";
            byte firstbyte = this.Reader.ReadByte();
            int firstbit = firstbyte & 0x1;
            for (int i = 0;i < this.cch;i++)
            {
                if (firstbit == 0)
                {
                    this.virtpathstring += (char)this.Reader.ReadByte();
                }
                else
                {
                    // read 1 byte per char
                    // read two byte per char
                    this.virtpathstring += System.BitConverter.ToChar(this.Reader.ReadBytes(2), 0);
                } 
            }
            this.virtpathstring = ExcelHelperClass.parseVirtualPath(this.virtpathstring);
        }
         
        if (this.virtpathstring != null)
        {
            if (this.virtpathstring.equals(0x00))
            {
                this.isselfreferencing = true;
            }
            else if (this.virtpathstring.equals(0x20))
            {
                this.isunusedsupportinglink = true;
            }
            else
            {
                this.isexternalworkbookreferencing = true;
            }  
        }
         
        if ((this.isexternalworkbookreferencing) || (this.isunusedsupportinglink))
        {
            this.rgst = new String[this.ctab];
            for (int i = 0;i < this.ctab;i++)
            {
                UInt16 cch2 = this.Reader.ReadUInt16();
                byte firstbyte = this.Reader.ReadByte();
                int firstbit = firstbyte & 0x1;
                for (int j = 0;j < cch2;j++)
                {
                    if (firstbit == 0)
                    {
                        this.rgst[i] = this.rgst[i] + (char)this.Reader.ReadByte();
                    }
                    else
                    {
                        // read 1 byte per char
                        // read two byte per char
                        this.rgst[i] = this.rgst[i] + System.BitConverter.ToChar(this.Reader.ReadBytes(2), 0);
                    } 
                }
            }
        }
         
        if (this.virtpathstring != null && virtpathstring.length() > 1)
            this.isselfreferencing = false;
         
        // assert that the correct number of bytes has been read from the stream
        Debug.Assert(this.Offset + this.Length == this.Reader.BaseStream.Position);
    }

}


