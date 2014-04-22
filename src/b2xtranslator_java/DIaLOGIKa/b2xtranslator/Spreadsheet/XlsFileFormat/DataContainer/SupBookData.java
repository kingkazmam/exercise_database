//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:36 AM
//

package DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.DataContainer;

import DIaLOGIKa.b2xtranslator.CommonTranslatorLib.IMapping;
import DIaLOGIKa.b2xtranslator.CommonTranslatorLib.IVisitable;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.DataContainer.SupBookData;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.DataContainer.XCTData;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.CRN;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.ExternName;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.SupBook;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.XCT;

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
/**
* This class contains several information about the SUPBOOK BIFF Record
*/
public class SupBookData   implements IVisitable
{
    private String virtPath;
    public String getVirtPath() throws Exception {
        return this.virtPath;
    }

    private String[] rgst;
    public String[] getRGST() throws Exception {
        return this.rgst;
    }

    private boolean selfref;
    public boolean getSelfRef() throws Exception {
        return this.selfref;
    }

    private LinkedList<XCTData> xctDataList = new LinkedList<XCTData>();
    public LinkedList<XCTData> getXCTDataList() throws Exception {
        return this.xctDataList;
    }

    private LinkedList<String> externNames = new LinkedList<String>();
    public LinkedList<String> getExternNames() throws Exception {
        return this.externNames;
    }

    public int ExternalLinkId;
    public String ExternalLinkRef;
    public int Number;
    /**
    * Ctor
    * 
    *  @param supbook SUPBOOK BIFF Record
    */
    public SupBookData(SupBook supbook) throws Exception {
        this.rgst = supbook.rgst;
        this.virtPath = supbook.virtpathstring;
        this.selfref = supbook.isselfreferencing;
        this.xctDataList = new LinkedList<XCTData>();
        this.externNames = new LinkedList<String>();
    }

    /**
    * returns the value at the specified position
    * 
    *  @param index searched index
    *  @return
    */
    public String getRgstString(int index) throws Exception {
        return this.rgst[index];
    }

    /**
    * Add a XCT Data structure to the internal stack
    * 
    *  @param xct
    */
    public void addXCT(XCT xct) throws Exception {
        XCTData xctdata = new XCTData(xct);
        this.xctDataList.AddLast(xctdata);
    }

    public void addCRN(CRN crn) throws Exception {
        this.xctDataList.Last.Value.addCRN(crn);
    }

    public void addEXTERNNAME(ExternName extname) throws Exception {
        this.externNames.AddLast(extname.extName);
    }

    public <T>void convert(T mapping) throws Exception {
        ((IMapping<SupBookData>)mapping).apply(this);
    }

}


