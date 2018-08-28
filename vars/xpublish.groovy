/**
 * @param title title of the HTML report.
 * @param path releative path where the HTML report is located.
 * @param mainFile main HTML file (default: index.html)
 * @note the report is published when path and mainFile does exist only.
 */
void html(final String title, final String path, final String mainFile='index.html') {
    if (fileExists(path + '/' + mainFile)) {
        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false,
                    reportDir: path, reportFiles: mainFile,
                    reportName: title, reportTitles: title])
    }
}