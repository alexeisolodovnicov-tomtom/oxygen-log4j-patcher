/*
 * Copyright (c) 2021 Syncro Soft SRL - All Rights Reserved.
 *
 * This file contains proprietary and confidential source code.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package com.oxygenxml.patcher.log4j;

import java.io.File;
import java.io.IOException;

public abstract class Log4jSearcher {
  
  /**
   * The root folder to process, typically an Oxygen installation.
   */
  private File folderToProcess;

  /**
   * The folder to process.
   * 
   * @param folderToProcess The root folder to process, typically an Oxygen installation.
   */
  public Log4jSearcher(File folderToProcess) {
    this.folderToProcess = folderToProcess;
  }

  /**
   * Scans and replaces log4j jar files with the newer version and also changes
   * the references to the jar files.
   * 
   * @throws IOException When the replacement failed.
   */
  public int scanFiles() throws IOException {
    System.out.println("Start scanning.");
    int noOfChanges = scanFiles(folderToProcess);
    System.out.println("Performed " + noOfChanges + " changes.");
    return noOfChanges;
  }

  private int scanFiles(File folder) throws IOException {
    int noOfChanges = 0;
    File[] files = folder.listFiles();

    for (File file : files) {
      if (file.isDirectory()) {
        noOfChanges += scanFiles(file);
      } else {
        String fileName = file.getName();
        if (fileName.contains("log4j") && fileName.endsWith(".jar")) {
          noOfChanges += processLog4jFile(file);
        } else if (canContainLog4jReferences(fileName)) {
          processLog4jReferencesInContentOfFile(file);
          noOfChanges ++;
        }
      }
    }
    return noOfChanges;
  }

  protected abstract boolean canContainLog4jReferences(String fileName);

  protected abstract void processLog4jReferencesInContentOfFile(File file) throws IOException;

  protected abstract int processLog4jFile(File file) throws IOException;

}
