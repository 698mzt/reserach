(function ($) {
    "use strict";

    var defaults = {
        formSelector: "",
        approveButtonSelector: "",
        isApprovalPage: function () {
            return true;
        },
        editableNames: [],
        editableSelectors: "",
        excludeNames: [],
        excludeSelectors: "",
        onApprove: function () {},
        modalTitle: "确认审批修改内容"
    };

    function normalizeText(value) {
        if (value === undefined || value === null) {
            return "";
        }
        return $.trim(String(value));
    }

    function displayText(value) {
        value = normalizeText(value);
        return value === "" ? "空" : escapeHtml(value);
    }

    function escapeHtml(value) {
        return String(value)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function isIgnoredControl($field) {
        var name = $field.attr("name");
        var type = ($field.attr("type") || "").toLowerCase();

        return !name || type === "hidden" || type === "file" || type === "button" || type === "submit" || type === "reset";
    }

    function hasExplicitEditableConfig(opts) {
        return (opts.editableNames && opts.editableNames.length > 0) || !!opts.editableSelectors;
    }

    function isExplicitlyEditable($field, opts) {
        var name = $field.attr("name");

        if (opts.editableNames && $.inArray(name, opts.editableNames) !== -1) {
            return true;
        }
        if (opts.editableSelectors && $field.is(opts.editableSelectors)) {
            return true;
        }
        return false;
    }

    function isLockedControl($field) {
        return $field.is(":disabled") || $field.is("[readonly]");
    }

    function isExcluded($field, opts) {
        var name = $field.attr("name");

        if (isIgnoredControl($field)) {
            return true;
        }
        if (opts.excludeNames && $.inArray(name, opts.excludeNames) !== -1) {
            return true;
        }
        if (opts.excludeSelectors && $field.is(opts.excludeSelectors)) {
            return true;
        }
        return false;
    }

    function shouldManageField($field, opts) {
        if (isExcluded($field, opts)) {
            return false;
        }
        if (hasExplicitEditableConfig(opts)) {
            return isExplicitlyEditable($field, opts);
        }
        return isLockedControl($field);
    }

    function getFieldLabel($field) {
        var label = $field.closest(".form-group").find("label").first().text();
        label = normalizeText(label).replace(/[：:]\s*$/, "");
        return label || $field.attr("name");
    }

    function getFieldValue($fields) {
        var $first = $fields.first();
        var tagName = ($first.prop("tagName") || "").toLowerCase();
        var type = ($first.attr("type") || "").toLowerCase();

        if (type === "radio") {
            var $checkedRadio = $fields.filter(":checked").first();
            if ($checkedRadio.length === 0) {
                return "";
            }
            return normalizeText($checkedRadio.closest("label").text()) || $checkedRadio.val();
        }

        if (type === "checkbox") {
            var values = [];
            $fields.filter(":checked").each(function () {
                var $item = $(this);
                values.push(normalizeText($item.closest("label").text()) || $item.val());
            });
            return values.join("、");
        }

        if (tagName === "select") {
            var selectValues = [];
            $first.find("option:selected").each(function () {
                selectValues.push(normalizeText($(this).text()));
            });
            return selectValues.join("、");
        }

        return $first.val();
    }

    function findFormControls($form) {
        return $form.find("input, select, textarea");
    }

    function markManagedFields($form, opts) {
        findFormControls($form).each(function () {
            var $field = $(this);
            $field.data("approvalEditManaged", shouldManageField($field, opts));
        });
    }

    function findManagedFields($form) {
        return findFormControls($form).filter(function () {
            return $(this).data("approvalEditManaged") === true;
        });
    }

    function enableFields($form, opts) {
        findManagedFields($form).each(function () {
            var $field = $(this);
            $field.prop("readonly", false);
            $field.prop("disabled", false);
            $field.removeAttr("readonly");
            $field.removeAttr("disabled");
        });
    }

    function findManagedFieldsByName($form, name) {
        return findManagedFields($form).filter(function () {
            return $(this).attr("name") === name;
        });
    }

    function captureValues($form, opts) {
        var values = {};
        findManagedFields($form).each(function () {
            var $field = $(this);
            var name = $field.attr("name");
            if (!values[name]) {
                var $group = findManagedFieldsByName($form, name);
                values[name] = {
                    label: getFieldLabel($field),
                    value: normalizeText(getFieldValue($group))
                };
            }
        });
        return values;
    }

    function collectChanges($form, opts) {
        var originalValues = $form.data("approvalOriginalValues") || {};
        var currentValues = captureValues($form, opts);
        var changes = [];

        $.each(currentValues, function (name, current) {
            var original = originalValues[name] || { label: current.label, value: "" };
            if (normalizeText(original.value) !== normalizeText(current.value)) {
                changes.push({
                    name: name,
                    label: current.label || original.label || name,
                    oldValue: original.value,
                    newValue: current.value
                });
            }
        });

        return changes;
    }

    function buildModalContent(changes) {
        var rows = $.map(changes, function (item) {
            return "<tr>" +
                "<td style=\"white-space:nowrap;\">" + escapeHtml(item.label) + "</td>" +
                "<td style=\"word-break:break-all;\">" + displayText(item.oldValue) + "</td>" +
                "<td style=\"word-break:break-all;\">" + displayText(item.newValue) + "</td>" +
                "</tr>";
        }).join("");

        return "<div style=\"padding:10px 15px;max-height:420px;overflow:auto;\">" +
            "<table class=\"table table-bordered table-striped\" style=\"margin-bottom:0;\">" +
            "<thead><tr><th style=\"width:22%;\">字段</th><th style=\"width:39%;\">原值</th><th style=\"width:39%;\">修改后</th></tr></thead>" +
            "<tbody>" + rows + "</tbody>" +
            "</table>" +
            "</div>";
    }

    function confirmChanges(opts, changes, submitFn) {
        if (!changes || changes.length === 0) {
            submitFn();
            return;
        }

        layer.open({
            type: 1,
            title: opts.modalTitle,
            area: ["720px", "520px"],
            shadeClose: false,
            btn: ["确认通过", "取消"],
            content: buildModalContent(changes),
            yes: function (index) {
                layer.close(index);
                submitFn();
            }
        });
    }

    $.approvalEditConfirm = {
        init: function (options) {
            var opts = $.extend({}, defaults, options || {});
            var $form = $(opts.formSelector);
            if ($form.length === 0) {
                return;
            }

            if (opts.isApprovalPage()) {
                markManagedFields($form, opts);
                enableFields($form, opts);
            } else {
                findFormControls($form).data("approvalEditManaged", false);
            }
            $form.data("approvalOptions", opts);
            $form.data("approvalOriginalValues", captureValues($form, opts));

            if (opts.approveButtonSelector) {
                $(document).off("click.approvalEditConfirm", opts.approveButtonSelector)
                    .on("click.approvalEditConfirm", opts.approveButtonSelector, function (event) {
                        event.preventDefault();
                        $.approvalEditConfirm.submit(opts.formSelector);
                    });
            }
        },
        submit: function (formSelector) {
            var $form = $(formSelector);
            var opts = $form.data("approvalOptions");
            if (!opts) {
                return;
            }
            if ($.validate && $.validate.form && !$.validate.form()) {
                return;
            }
            confirmChanges(opts, collectChanges($form, opts), opts.onApprove);
        },
        collectChanges: function (formSelector) {
            var $form = $(formSelector);
            var opts = $form.data("approvalOptions");
            return opts ? collectChanges($form, opts) : [];
        }
    };
})(jQuery);
