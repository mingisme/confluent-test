package com.swang.lab;

import java.util.Base64;

public class DecodeBase64 {
    public static void main(String[] args) {
        String schemaAsString="ewogICJkb2MiOiAiR2VuZXJpYyBhcHBsaWNhdGlvbiBsb2cgZXZlbnQiLAogICJmaWVsZHMiOiBbCiAgICB7CiAgICAgICJkb2MiOiAiVGltZXN0YW1wIG9mIHRoZSBldmVudCIsCiAgICAgICJuYW1lIjogInRpbWVzdGFtcCIsCiAgICAgICJ0eXBlIjogImxvbmciCiAgICB9LAogICAgewogICAgICAiZG9jIjogIlRoZSBhcHBsaWNhdGlvbiB0aGF0IHNlbnQgdGhlIGV2ZW50IiwKICAgICAgIm5hbWUiOiAic291cmNlIiwKICAgICAgInR5cGUiOiB7CiAgICAgICAgImRvYyI6ICJJZGVudGlmaWNhdGlvbiBvZiBhbiBhcHBsaWNhdGlvbiIsCiAgICAgICAgImZpZWxkcyI6IFsKICAgICAgICAgIHsKICAgICAgICAgICAgImRvYyI6ICJUaGUgbmFtZSBvZiB0aGUgYXBwbGljYXRpb24iLAogICAgICAgICAgICAibmFtZSI6ICJuYW1lIiwKICAgICAgICAgICAgInR5cGUiOiAic3RyaW5nIgogICAgICAgICAgfSwKICAgICAgICAgIHsKICAgICAgICAgICAgImRvYyI6ICJUaGUgYXBwbGljYXRpb24gdmVyc2lvbiIsCiAgICAgICAgICAgICJuYW1lIjogInZlcnNpb24iLAogICAgICAgICAgICAidHlwZSI6ICJzdHJpbmciCiAgICAgICAgICB9LAogICAgICAgICAgewogICAgICAgICAgICAiZG9jIjogIlRoZSBvd25lciBvZiB0aGUgYXBwbGljYXRpb24iLAogICAgICAgICAgICAibmFtZSI6ICJvd25lciIsCiAgICAgICAgICAgICJ0eXBlIjogInN0cmluZyIKICAgICAgICAgIH0KICAgICAgICBdLAogICAgICAgICJuYW1lIjogIkFwcGxpY2F0aW9uIiwKICAgICAgICAidHlwZSI6ICJyZWNvcmQiCiAgICAgIH0KICAgIH0sCiAgICB7CiAgICAgICJkb2MiOiAiVGhlIGFwcGxpY2F0aW9uIGNvbnRleHQsIGNvbnRhaW5zIGFwcGxpY2F0aW9uLXNwZWNpZmljIGtleS12YWx1ZSBwYWlycyIsCiAgICAgICJuYW1lIjogImNvbnRleHQiLAogICAgICAidHlwZSI6IHsKICAgICAgICAidHlwZSI6ICJtYXAiLAogICAgICAgICJ2YWx1ZXMiOiAic3RyaW5nIgogICAgICB9CiAgICB9LAogICAgewogICAgICAiZG9jIjogIlRoZSBsb2cgbGV2ZWwsIGJlaW5nIGVpdGhlciBERUJVRywgSU5GTywgV0FSTiBvciBFUlJPUiIsCiAgICAgICJuYW1lIjogImxldmVsIiwKICAgICAgInR5cGUiOiB7CiAgICAgICAgImRvYyI6ICJUaGUgbGV2ZWwgb2YgdGhlIGxvZyBtZXNzYWdlIiwKICAgICAgICAibmFtZSI6ICJBcHBsaWNhdGlvbkxvZ0xldmVsIiwKICAgICAgICAic3ltYm9scyI6IFsKICAgICAgICAgICJERUJVRyIsCiAgICAgICAgICAiSU5GTyIsCiAgICAgICAgICAiV0FSTiIsCiAgICAgICAgICAiRVJST1IiLAogICAgICAgICAgIkZBVEFMIgogICAgICAgIF0sCiAgICAgICAgInR5cGUiOiAiZW51bSIKICAgICAgfQogICAgfSwKICAgIHsKICAgICAgImRvYyI6ICJUaGUgbG9nIG1lc3NhZ2UiLAogICAgICAibmFtZSI6ICJtZXNzYWdlIiwKICAgICAgInR5cGUiOiAic3RyaW5nIgogICAgfQogIF0sCiAgIm5hbWUiOiAiQXBwbGljYXRpb25Mb2dFdmVudCIsCiAgIm5hbWVzcGFjZSI6ICJpby5heHVhbC5jbGllbnQuZXhhbXBsZS5zY2hlbWEiLAogICJ0eXBlIjogInJlY29yZCIKfQ==";
        schemaAsString = new String(Base64.getDecoder().decode(schemaAsString));
        System.out.println(schemaAsString);
    }
}
