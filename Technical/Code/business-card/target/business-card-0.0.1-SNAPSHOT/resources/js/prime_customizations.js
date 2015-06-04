
PrimeFaces.widget.Paginator = PrimeFaces.widget.BaseWidget
		.extend({
			init : function(b) {
				this.cfg = b;
				this.jq = $();
				var a = this;
				$.each(this.cfg.id, function(c, d) {
					a.jq = a.jq.add($(PrimeFaces.escapeClientId(d)))
				});
				this.pagesContainer = this.jq.children(".ui-paginator-pages");
				this.pageLinks = this.pagesContainer
						.children(".ui-paginator-page");
				this.rppSelect = this.jq.children(".ui-paginator-rpp-options");
				this.jtpSelect = this.jq.children(".ui-paginator-jtp-select");
				this.firstLink = this.jq.children(".ui-paginator-first");
				this.prevLink = this.jq.children(".ui-paginator-prev");
				this.nextLink = this.jq.children(".ui-paginator-next");
				this.endLink = this.jq.children(".ui-paginator-last");
				this.currentReport = this.jq.children(".ui-paginator-current");
				this.cfg.rows = this.cfg.rows == 0 ? this.cfg.rowCount
						: this.cfg.rows;
				this.cfg.pageCount = Math.ceil(this.cfg.rowCount
						/ this.cfg.rows) || 1;
				this.cfg.pageLinks = this.cfg.pageLinks || 10;
				this.cfg.currentPageTemplate = this.cfg.currentPageTemplate
						|| "({currentPage} of {totalPages})";
				this.bindEvents()
			},
			bindEvents : function() {
				var a = this;
				this.jq.children("span.ui-state-default").on(
						"mouseover.paginator", function() {
							var b = $(this);
							if (!b.hasClass("ui-state-disabled")) {
								b.addClass("ui-state-hover")
							}
						}).on("mouseout.paginator", function() {
					$(this).removeClass("ui-state-hover")
				}).on("focus.paginator", function() {
					$(this).addClass("ui-state-focus")
				}).on("blur.paginator", function() {
					$(this).removeClass("ui-state-focus")
				}).on("keydown.paginator", function(d) {
					var b = d.which, c = $.ui.keyCode;
					if ((b === c.ENTER || b === c.NUMPAD_ENTER)) {
						$(this).trigger("click");
						d.preventDefault()
					}
				});
				this.bindPageLinkEvents();
				PrimeFaces.skinSelect(this.rppSelect);
				this.rppSelect.change(function(b) {
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setRowsPerPage(parseInt($(this).val()))
					}
				});
				PrimeFaces.skinSelect(this.jtpSelect);
				this.jtpSelect.change(function(b) {
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setPage(parseInt($(this).val()))
					}
				});
				this.firstLink.click(function() {
					PrimeFaces.clearSelection();
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setPage(0)
					}
				});
				this.prevLink.click(function() {
					PrimeFaces.clearSelection();
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setPage(a.cfg.page - 1)
					}
				});
				this.nextLink.click(function() {
					PrimeFaces.clearSelection();
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setPage(a.cfg.page + 1)
					}
				});
				this.endLink.click(function() {
					PrimeFaces.clearSelection();
					if (!$(this).hasClass("ui-state-disabled")) {
						a.setPage(a.cfg.pageCount - 1)
					}
				})
			},
			bindPageLinkEvents : function() {
				var a = this;
				this.pagesContainer.children(".ui-paginator-page").on(
						"click.paginator",
						function(c) {
							var b = $(this);
							if (!b.hasClass("ui-state-disabled")
									&& !b.hasClass("ui-state-active")) {
								a.setPage(parseInt(b.text()) - 1)
							}
						}).on(
						"mouseover.paginator",
						function() {
							var b = $(this);
							if (!b.hasClass("ui-state-disabled")
									&& !b.hasClass("ui-state-active")) {
								b.addClass("ui-state-hover")
							}
						}).on("mouseout.paginator", function() {
					$(this).removeClass("ui-state-hover")
				}).on("focus.paginator", function() {
					$(this).addClass("ui-state-focus")
				}).on("blur.paginator", function() {
					$(this).removeClass("ui-state-focus")
				}).on("keydown.paginator", function(d) {
					var b = d.which, c = $.ui.keyCode;
					if ((b === c.ENTER || b === c.NUMPAD_ENTER)) {
						$(this).trigger("click");
						d.preventDefault()
					}
				})
			},
			updateUI : function() {
				if (this.cfg.page === 0) {
					this.disableElement(this.firstLink);
					this.disableElement(this.prevLink)
				} else {
					this.enableElement(this.firstLink);
					this.enableElement(this.prevLink)
				}
				if (this.cfg.page === (this.cfg.pageCount - 1)) {
					this.disableElement(this.nextLink);
					this.disableElement(this.endLink)
				} else {
					this.enableElement(this.nextLink);
					this.enableElement(this.endLink)
				}
				var a = (this.cfg.rowCount === 0) ? 0
						: (this.cfg.page * this.cfg.rows) + 1, c = (this.cfg.page * this.cfg.rows)
						+ this.cfg.rows;
				if (c > this.cfg.rowCount) {
					c = this.cfg.rowCount
				}
				var d = this.cfg.currentPageTemplate.replace("{currentPage}",
						this.cfg.page + 1).replace("{totalPages}",
						this.cfg.pageCount).replace("{totalRecords}",
						this.cfg.rowCount).replace("{startRecord}", a).replace(
						"{endRecord}", c);
				this.currentReport.text(d);
				this.rppSelect.children("option").prop("selected", false)
						.filter("option[value=" + this.cfg.rows + "]").prop(
								"selected", true);
				if (this.jtpSelect.length > 0) {
					this.jtpSelect.children().remove();
					for (var b = 0; b < this.cfg.pageCount; b++) {
						this.jtpSelect.append("<option value=" + b + ">"
								+ (b + 1) + "</option>")
					}
					this.jtpSelect.children(
							"option[value=" + (this.cfg.page) + "]").prop(
							"selected", "selected")
				}
				this.updatePageLinks()
			},
			updatePageLinks : function() {
				var a, b, k, h = $(document.activeElement), c, d;
				if (h.hasClass("ui-paginator-page")) {
					var j = this.pagesContainer.index(h.parent());
					if (j >= 0) {
						c = this.pagesContainer.eq(j);
						d = h.index()
					}
				}
				this.cfg.pageCount = Math.ceil(this.cfg.rowCount
						/ this.cfg.rows) || 1;
				var g = Math.min(this.cfg.pageLinks, this.cfg.pageCount);
				a = Math.max(0, Math.ceil(this.cfg.page - ((g) / 2)));
				b = Math.min(this.cfg.pageCount - 1, a + g - 1);
				k = this.cfg.pageLinks - (b - a + 1);
				a = Math.max(0, a - k);
				this.pagesContainer.children().remove();
				for (var e = a; e <= b; e++) {
					var f = "ui-paginator-page ui-state-default ui-corner-all";
					if (this.cfg.page == e) {
						f += " ui-state-active"
					}
					this.pagesContainer.append('<span class="' + f
							+ '" tabindex="0">' + (e + 1) + "</span>")
				}
				if (c) {
					c.children().eq(d).trigger("focus")
				}
				this.bindPageLinkEvents()
			},
			setPage : function(c, a) {
				if (c >= 0 && c < this.cfg.pageCount && this.cfg.page != c) {
					var b = {
						first : this.cfg.rows * c,
						rows : this.cfg.rows,
						page : c
					};
					if (a) {
						this.cfg.page = c;
						this.updateUI()
					} else {
						this.cfg.paginate.call(this, b)
					}
				}
			},
			setRowsPerPage : function(b) {
				var c = this.cfg.rows * this.cfg.page, a = parseInt(c / b);
				this.cfg.rows = b;
				this.cfg.pageCount = Math.ceil(this.cfg.rowCount
						/ this.cfg.rows);
				this.cfg.page = -1;
				this.setPage(a)
			},
			setTotalRecords : function(a) {
				this.cfg.rowCount = a;
				this.cfg.pageCount = Math.ceil(a / this.cfg.rows) || 1;
				this.cfg.page = 0;
				this.updateUI()
			},
			getCurrentPage : function() {
				return this.cfg.page
			},
			getFirst : function() {
				return (this.cfg.rows * this.cfg.page)
			},
			getContainerHeight : function(c) {
				var a = 0;
				for (var b = 0; b < this.jq.length; b++) {
					a += this.jq.eq(b).outerHeight(c)
				}
				return a
			},
			disableElement : function(a) {
				a.removeClass("ui-state-hover ui-state-focus ui-state-active")
						.addClass("ui-state-disabled").attr("tabindex", -1);
				a.removeClass("ui-state-hover ui-state-focus ui-state-active")
						.addClass("ui-state-disabled").attr("tabindex", -1)
			},
			enableElement : function(a) {
				a.removeClass("ui-state-disabled").attr("tabindex", 0)
			}
		});