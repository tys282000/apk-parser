package net.dongliu.apk.parser.struct.resource;

/**
 * used by resource Type.
 *
 * @author dongliu
 */
public class ResTableConfig {
    private byte[] array;
    // Number of bytes in this structure. uint32_t
    private int size;

    // Mobile country code (from SIM).  0 means "any". uint16_t
    private short mcc;
    // Mobile network code (from SIM).  0 means "any". uint16_t
    private short mnc;
    //uint32_t imsi;

    // 0 means "any".  Otherwise, en, fr, etc. char[2]
    private String language;
    // 0 means "any".  Otherwise, US, CA, etc.  char[2]
    private String country;
    // uint32_t locale;

    // uint8_t
    private short orientation;
    // uint8_t
    private short touchscreen;
    // uint16_t
    private int density;
    // uint32_t screenType;

    // uint8_t
    private byte keyboard;
    // uint8_t
    private byte navigation;
    // uint8_t
    private byte inputFlags;
    // uint8_t
    private byte inputPad0;
    // uint32_t input;

    // uint16_t
    private int screenWidth;
    // uint16_t
    private int screenHeight;
    // uint32_t screenSize;

    // uint16_t
    private int sdkVersion;
    // For now minorVersion must always be 0!!!  Its meaning is currently undefined.
    // uint16_t
    private int minorVersion;
    //uint32_t version;

    // uint8_t
    private byte screenLayout;
    // uint8_t
    private byte uiMode;
    // uint8_t
    private short screenConfigPad1;
    // uint8_t
    private short screenConfigPad2;
    //uint32_t screenConfig;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public short getMcc() {
        return mcc;
    }

    public void setMcc(short mcc) {
        this.mcc = mcc;
    }

    public short getMnc() {
        return mnc;
    }

    public void setMnc(short mnc) {
        this.mnc = mnc;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public short getOrientation() {
        return orientation;
    }

    public void setOrientation(short orientation) {
        this.orientation = orientation;
    }

    public short getTouchscreen() {
        return touchscreen;
    }

    public void setTouchscreen(short touchscreen) {
        this.touchscreen = touchscreen;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public short getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(byte keyboard) {
        this.keyboard = keyboard;
    }

    public short getNavigation() {
        return navigation;
    }

    public void setNavigation(byte navigation) {
        this.navigation = navigation;
    }

    public short getInputFlags() {
        return inputFlags;
    }

    public void setInputFlags(byte inputFlags) {
        this.inputFlags = inputFlags;
    }

    public short getInputPad0() {
        return inputPad0;
    }

    public void setInputPad0(byte inputPad0) {
        this.inputPad0 = inputPad0;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public short getScreenLayout() {
        return screenLayout;
    }

    public void setScreenLayout(byte screenLayout) {
        this.screenLayout = screenLayout;
    }

    public short getUiMode() {
        return uiMode;
    }

    public void setUiMode(byte uiMode) {
        this.uiMode = uiMode;
    }

    public short getScreenConfigPad1() {
        return screenConfigPad1;
    }

    public void setScreenConfigPad1(short screenConfigPad1) {
        this.screenConfigPad1 = screenConfigPad1;
    }

    public short getScreenConfigPad2() {
        return screenConfigPad2;
    }

    public void setScreenConfigPad2(short screenConfigPad2) {
        this.screenConfigPad2 = screenConfigPad2;
    }

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ResTableConfig) {
            ResTableConfig oldResConfig = (ResTableConfig) object;
            if (size != oldResConfig.getSize()) {
                return false;
            }
            byte[] oldArray = oldResConfig.getArray();
            for (int i = 0; i < size; i++) {
                if (array[i] != oldArray[i]) {
//                    System.out.println("i:"+ i + " config:" + array[i] + " old:" + oldArray[i]);
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
