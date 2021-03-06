/*-------------------------------------------------------------------------------------------------------------------*\
|  Copyright (C) 2014 eBay Software Foundation                                                                        |
|                                                                                                                     |
|  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance     |
|  with the License.                                                                                                  |
|                                                                                                                     |
|  You may obtain a copy of the License at                                                                            |
|                                                                                                                     |
|       http://www.apache.org/licenses/LICENSE-2.0                                                                    |
|                                                                                                                     |
|  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed   |
|  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for  |
|  the specific language governing permissions and limitations under the License.                                     |
\*-------------------------------------------------------------------------------------------------------------------*/

package com.paypal.selion.grid;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.selion.pojos.SeLionGridConstants;

public class FileDownloaderTest {

    File downloadedFile = null;
    File homeDir = null;
    
    @BeforeClass
    public void createHomeDir() throws IOException{
        //Re-setting the path -just in case if any other test changes the SeLion_home_dir constant
        SeLionGridConstants.SELION_HOME_DIR= SystemUtils.USER_HOME + "/.selion/";
        homeDir = new File(SeLionGridConstants.SELION_HOME_DIR+"/downloads/");
        homeDir.mkdirs();
    }
    
    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testUnsupportedFileType() {
        // gz compression type is not supported.
        String unsupportedFileURL = "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-1.9.7-linux-i686.tar.gz";
        FileDownloader.downloadFile(unsupportedFileURL, "123CEFRGxSdfnsfwefla");
    }

    @Test
    public void testFileDownload() {
        String testUrl = "https://selenium-release.storage.googleapis.com/2.45/IEDriverServer_Win32_2.45.0.zip";
        String tempChecksum = "dde210e04e5c1b0d6019fd8a1199df18";
        String result = FileDownloader.downloadFile(testUrl, tempChecksum);
        downloadedFile = new File(result);
        assertTrue(downloadedFile.exists());
    }
    
    @Test
    public void testInvalidChecksum() {
        String testUrl = "https://chromedriver.storage.googleapis.com/2.14/chromedriver_win32.zip";
        String tempChecksum = "dde210e04e5c1b0d6019fd8a1199df18";
        String result = FileDownloader.downloadFile(testUrl, tempChecksum);
        assertNull(result);
    }

    @AfterClass(alwaysRun = true)
    public void cleanUpFile() {
      FileUtils.deleteQuietly(downloadedFile);
      FileUtils.deleteQuietly(homeDir);
    }
}
