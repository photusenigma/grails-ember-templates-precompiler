class EmberTemplatesPrecompilerGrailsPlugin {
    def version = "1.0.0"
    def grailsVersion = "1.3.5 > *"
    def dependsOn = [resources:'1.0 > *']
    def loadAfter = ['resources']
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def title = "EmberJS templates precompiler Plugin"
    def author = "Florent BLANVILLAIN"
    def authorEmail = "florent.blanvillain@gmail.com"
    def description = '''\
This plug-in precompiles Ember.js handlebars templates on the server side.

It integrates with grails resources plugin to allow subsequent minification, gzip and more.

It's a fork from Matt Sheehan's grails-handlebars-resources plugin with two differences :
- it targets Handlebars templates with added EmberJS helpers
- it does not declare any resource module.
'''
    def documentation = "https://github.com/florent-blanvillain/grails-ember-templates-precompiler/blob/master/README.md"
    def license = "APACHE"
    def issueManagement = [ system: "github", url: "https://github.com/florent-blanvillain/grails-ember-templates-precompiler/issues" ]
    def scm = [ url: "https://github.com/florent-blanvillain/grails-ember-templates-precompiler" ]
}