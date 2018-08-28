/**
 * @param title title of the HTML report.
 * @param path releative path where the HTML report is located.
 * @param mainFile main HTML file (default: index.html)
 */
void html(final String title, final String path, final String mainFile='index.html') {
    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false,
                 reportDir: path, reportFiles: mainFile,
                 reportName: title, reportTitles: title])
}