package com.adobe.aem.demo.utils.models;

/**
 * Sling Model to expose critical metadata about the current version of AEM / DemoUtils
 **/
public interface VersionMetadata {
    /***
     * @return the current version of DemoUtils installed
     */
    String getUtilsVersion();

    default String getAemType() { return "unknown"; }

    default String getFullVersion() { return "unknown"; }

    default String getYearMonthDayVersion() { return "unknown"; }

    default String getMajorVersion() { return "unknown"; }

    default String getMinorVersion() { return "unknown"; }

    default String getMicroVersion() { return "unknown"; }

    default String getQualifierVersion() { return "unknown"; }
}