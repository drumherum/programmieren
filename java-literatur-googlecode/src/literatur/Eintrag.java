/**
 * java-literatur: literatur.Eintrag.java
 * 
 * 06.03.2009, Harald R. Haberstroh
 */

package literatur;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 1. autor - String(100) 2. titel - String(100) 3. erscheinungsjahr - int 4.
 * verlag - String(30) 5. zeitschrift - String(30) 6. seiten - String(10) 7.
 * link - String(60)
 * 
 * @author Harald R. Haberstroh (hp)
 * 
 */
public class Eintrag {

  // Feldlängen
  private final static int autorLen = 100;
  private final static int titelLen = 100;
  private final static int verlagLen = 30;
  private final static int zeitschriftLen = 30;
  private final static int seitenLen = 10;
  private final static int linkLen = 60;

  // Felder
  private String autor;
  private String titel;
  private int erscheinungsjahr;
  private String verlag;
  private String zeitschrift;
  private String seiten;
  private String link;

  // gelöscht?
  private boolean istGeloescht = false;

  /**
   * markiere als gelöscht
   */
  public void loeschen() {
    istGeloescht = true;
  }

  /**
   * liefert die Länge eines Datensatzes. Stringlängen * 3 + Längeninfo (Annahme
   * long) + int-Länge + Länge von Boolean (nehme int an...).
   * 
   * @return Datensatzlänge
   */
  public static int eintragLen() {
    int len = Integer.SIZE
        * 8
        + (autorLen + titelLen + verlagLen + zeitschriftLen + seitenLen + linkLen)
        * 3 + 6 * Long.SIZE * 8 + Integer.SIZE * 8;
    return len;
  }

  /**
   * schreibe Datensatz.
   * 
   * @param out
   *          Datei
   * @throws IOException
   *           Ein-/Ausgabefehler
   */
  public void write(RandomAccessFile out) throws IOException {
    out.writeBoolean(istGeloescht);
    out.writeUTF(autor);
    out.writeUTF(link);
    out.writeUTF(seiten);
    out.writeUTF(titel);
    out.writeUTF(verlag);
    out.writeUTF(zeitschrift);
    out.writeInt(erscheinungsjahr);
  }

  /**
   * lies Datein in aktuellen Satz.
   * 
   * @param in
   *          Datei
   * @throws IOException
   *           Ein-/Ausgabefehler
   */
  public void read(RandomAccessFile in) throws IOException, EOFException {
    istGeloescht = in.readBoolean();
    autor = in.readUTF();
    link = in.readUTF();
    seiten = in.readUTF();
    titel = in.readUTF();
    verlag = in.readUTF();
    zeitschrift = in.readUTF();
    erscheinungsjahr = in.readInt();
  }

  /**
   * @return the autor
   */
  public String getAutor() {
    return autor.trim();
  }

  /**
   * @param autor
   *          the autor to set
   */
  public void setAutor(String autor) {
    this.autor = fixed(autor, autorLen);
  }

  /**
   * @return the titel
   */
  public String getTitel() {
    return titel.trim();
  }

  /**
   * @param titel
   *          the titel to set
   */
  public void setTitel(String titel) {
    this.titel = fixed(titel, titelLen);
  }

  /**
   * @return the erscheinungsjahr
   */
  public int getErscheinungsjahr() {
    return erscheinungsjahr;
  }

  /**
   * @param erscheinungsjahr
   *          the erscheinungsjahr to set
   */
  public void setErscheinungsjahr(int erscheinungsjahr) {
    this.erscheinungsjahr = erscheinungsjahr;
  }

  /**
   * @return the verlag
   */
  public String getVerlag() {
    return verlag.trim();
  }

  /**
   * @param verlag
   *          the verlag to set
   */
  public void setVerlag(String verlag) {
    this.verlag = fixed(verlag, verlagLen);
  }

  /**
   * @return the zeitschrift
   */
  public String getZeitschrift() {
    return zeitschrift.trim();
  }

  /**
   * @param zeitschrift
   *          the zeitschrift to set
   */
  public void setZeitschrift(String zeitschrift) {
    this.zeitschrift = fixed(zeitschrift, zeitschriftLen);
  }

  /**
   * @return the seiten
   */
  public String getSeiten() {
    return seiten.trim();
  }

  /**
   * @param seiten
   *          the seiten to set
   */
  public void setSeiten(String seiten) {
    this.seiten = fixed(seiten, seitenLen);
  }

  /**
   * @return the link
   */
  public String getLink() {
    return link.trim();
  }

  /**
   * @param link
   *          the link to set
   */
  public void setLink(String link) {
    this.link = fixed(link, linkLen);
  }

  /**
   * @return the istGeloescht
   */
  public boolean istGeloescht() {
    return istGeloescht;
  }

  /**
   * Abschneiden oder Auffüllen auf fixe Länge
   * 
   * @param s
   *          String
   * @param len
   *          Länge
   * @return fixer String der Länge len
   */
  private String fixed(String s, int len) {
    int slen = s.length();
    if (slen < len) { // erweitern
      s += blank(len - slen);
    } else if (slen > len) { // abschneiden
      s = s.substring(0, len);
    }
    return s;
  }

  /**
   * erzeuge anz Blanks
   * 
   * @param anz
   * @return
   */
  private String blank(int anz) {
    StringBuffer sb = new StringBuffer(anz);
    for (int i = 0; i < anz; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }

  /**
   * @param autor
   * @param erscheinungsjahr
   * @param link
   * @param seiten
   * @param titel
   * @param verlag
   * @param zeitschrift
   */
  public Eintrag(String autor, int erscheinungsjahr, String link,
      String seiten, String titel, String verlag, String zeitschrift) {
    setAutor(autor);
    setErscheinungsjahr(erscheinungsjahr);
    setLink(link);
    setSeiten(seiten);
    setTitel(titel);
    setVerlag(verlag);
    setZeitschrift(zeitschrift);
  }

  /**
   * Defaultkonstruktor
   */
  public Eintrag() {
    this("", 0, "", "", "", "", "");
  }
}
