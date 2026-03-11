package com.example.myadermoshop;

/* loaded from: classes.dex */
public class PdfFile {
    private final String location;
    private final String name;

    public PdfFile(String str, String str2) {
        this.name = str;
        this.location = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }
}