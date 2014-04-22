//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:48:27 AM
//

package DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping;

import DIaLOGIKa.b2xtranslator.OpenXmlLib.DrawingML.Dml;
import DIaLOGIKa.b2xtranslator.OpenXmlLib.DrawingML.Dml.Chart;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.CrtSequence;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.MarkerFormat;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.MarkerFormat.MarkerType;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.Records.Radar;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.SeriesFormatSequence;
import DIaLOGIKa.b2xtranslator.Spreadsheet.XlsFileFormat.SsSequence;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.AbstractChartGroupMapping;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.CatMapping;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.ChartContext;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.DataLabelMapping;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.DataPointMapping;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.ExcelContext;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.SeriesMapping;
import DIaLOGIKa.b2xtranslator.SpreadsheetMLMapping.ValMapping;

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
public class RadarChartMapping  extends AbstractChartGroupMapping 
{
    public RadarChartMapping(ExcelContext workbookContext, ChartContext chartContext, boolean is3DChart) throws Exception {
        super(workbookContext, chartContext, is3DChart);
    }

    public void apply(CrtSequence crtSequence) throws Exception {
        if (!(crtSequence.ChartType instanceof Radar))
        {
            throw new Exception("Invalid chart type");
        }
         
        Radar radar = crtSequence.ChartType instanceof Radar ? (Radar)crtSequence.ChartType : (Radar)null;
        // c:radarChart
        _writer.WriteStartElement(Chart.Prefix, Chart.ElRadarChart, Chart.Ns);
        {
            // c:radarStyle
            writeValueElement(Chart.ElRadarStyle,mapRadarStyle(crtSequence.SsSequence));
            for (SeriesFormatSequence seriesFormatSequence : this.getChartFormatsSequence().SeriesFormatSequences)
            {
                // c:varyColors: This setting needs to be ignored if the chart has
                //writeValueElement(Dml.Chart.ElVaryColors, crtSequence.ChartFormat.fVaried ? "1" : "0");
                // Radar Chart Series (CT_RadarSer)
                if (seriesFormatSequence.SerToCrt != null && seriesFormatSequence.SerToCrt.id == crtSequence.ChartFormat.idx)
                {
                    // c:ser
                    _writer.WriteStartElement(Chart.Prefix, Chart.ElSer, Chart.Ns);
                    // EG_SerShared
                    seriesFormatSequence.Convert(new SeriesMapping(this.getWorkbookContext(),this.getChartContext()));
                    for (int i = 1;i < seriesFormatSequence.SsSequence.size();i++)
                    {
                        // c:marker
                        // c:dPt (Data Points)
                        // write a dPt for each SsSequence
                        SsSequence sss = seriesFormatSequence.SsSequence.get(i);
                        sss.Convert(new DataPointMapping(this.getWorkbookContext(),this.getChartContext(),i - 1));
                    }
                    // c:dLbls (Data Labels)
                    this.getChartFormatsSequence().Convert(new DataLabelMapping(this.getWorkbookContext(),this.getChartContext(),seriesFormatSequence));
                    // c:cat (Category Axis Data)
                    seriesFormatSequence.Convert(new CatMapping(this.getWorkbookContext(),this.getChartContext(),Chart.ElCat));
                    // c:val
                    seriesFormatSequence.Convert(new ValMapping(this.getWorkbookContext(),this.getChartContext(),Chart.ElVal));
                    _writer.writeEndElement();
                }
                 
            }
            for (int axisId : crtSequence.ChartFormat.AxisIds)
            {
                // c:ser
                // Data Labels
                // Axis Ids
                writeValueElement(Chart.ElAxId,String.valueOf(axisId));
            }
        }
        _writer.writeEndElement();
    }

    private String mapRadarStyle(SsSequence ssSequence) throws Exception {
        String radarStyle = "standard";
        boolean hasMarker = (ssSequence == null) || (ssSequence.MarkerFormat != null && ssSequence.MarkerFormat.imk != MarkerType.NoMarker);
        if (hasMarker)
        {
            radarStyle = "marker";
        }
         
        return radarStyle;
    }

}


// TODO: radar fill style