package org.grails.plugin.ember.handlebars

import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.Function
import org.mozilla.javascript.Context
import org.mozilla.javascript.tools.shell.Global

class Precompiler {

    private Scriptable scope
    private Function precompile

    Precompiler() {
        ClassLoader classLoader = getClass().classLoader
        URL handlebars = classLoader.getResource('handlebars-1.0.0.js')
        URL emberTemplateCompiler = classLoader.getResource('ember-template-compiler.js')
        Context cx = Context.enter()
        cx.optimizationLevel = 9
        Global global = new Global()
        global.init cx
        scope = cx.initStandardObjects(global)
        cx.evaluateString scope, handlebars.text, handlebars.file, 1, null

        // wrap ember precompiler
        cx.evaluateString scope, """
var exports = {}; // for emberTemplateCompiler
(function() {
var Ember = { assert: function() {} };

(function() {
${emberTemplateCompiler.text}
})();

exports.precompile = Ember.Handlebars.precompile;
exports.EmberHandlebars = Ember.Handlebars;
})();
""", "", 1, null


        cx.evaluateString scope, """
function precompileEmberHandlebars(string) {
  return exports.precompile(string).toString();
}
""", "", 1, null


        precompile = scope.get("precompileEmberHandlebars", scope)
        Context.exit();
    }

    void precompile(File input, File target, templateName) {
        log.debug "pre-compiling: '${input}' -> '${templateName}'"
        String compiledTemplate = precompileTemplate(input.text)

        String output = """
(function(){
    Ember.TEMPLATES['$templateName'] = Ember.Handlebars.template($compiledTemplate);
}());
"""
        target.write output
    }

    String precompileTemplate(String contents) {
        call precompile, contents
    }

    private synchronized String call(Function fn, Object[] args) {
        Context.call(null, fn, scope, scope, args)
    }
}
