//
// Translated by CS2J (http://www.cs2j.com): 1/30/2014 10:47:59 AM
//

package DIaLOGIKa.b2xtranslator.PptFileFormat;

import DIaLOGIKa.b2xtranslator.OfficeDrawing.OfficeRecordAttribute;

public class HashCode10Atom  extends DIaLOGIKa.b2xtranslator.OfficeDrawing.Record 
{
    public uint hash;
    public HashCode10Atom(BinaryReader _reader, uint size, uint typeCode, uint version, uint instance) throws Exception {
        super(_reader, size, typeCode, version, instance);
        hash = this.Reader.ReadUInt32();
    }

}


