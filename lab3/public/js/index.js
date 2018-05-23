$(function () {

	$("#create-owner").click(function () {
		var owner = $("#owner-input").val();
		$.get("/new/" + owner, function (data) {
		}).done(function () {
			var allowedToAdd = true;
      $("#owners-list").find('option:selected').removeAttr("selected");
      $("#owners-list").children().each(function () {
        var storedOwner = $(this).text();
        if (storedOwner === "Add list owner") {
          $(this).remove();
        }
        if (storedOwner === owner) {
        	allowedToAdd = false;
				}
      });
      if (allowedToAdd) {
        $("#owners-list").append("<option value='" + owner + "' selected>" + owner + "</option>");
      }
			$("#owners-list").val(owner);
			$.get("/purchase/" + owner, function (data) {
				updateTable(data);
        $('#ownerModal').modal('hide');
			});
		}).fail(function (data, textStatus, xhr) {
			console.log("error", data.status);
			console.log("STATUS: " + xhr);
		});
	});

	$("#owners-list").change(function () {
		var owner = "";
		$("select option:selected").each(function () {
			owner += $(this).text();
		});

		$.get("/purchase/" + owner, function (data) {
			updateTable(data);
		});
	});

	$("#submit-synchronize").click(function () {
		var token = $("#token-input").val();
		$.get("/synchronize/" + token, function (data) {
		}).done(function () {
			location.reload();
		}).fail(function (data, textStatus, xhr) {
			console.log("error", data.status);
			console.log("STATUS: " + xhr);
		});
	});

	$("#add-item").click(function () {
		var title = $("#title-input").val();
		var quantity = $("#quantity-input").val();
		var owner = $("#owners-list").find(":selected").text();

		var purchaseData = {
			"owner": owner,
			"items":
				[
					{
						"name": title,
						"quantity": quantity
					}
				]
		};

		$.ajax({
			type: "POST",
			url: "/purchase",
			data: JSON.stringify(purchaseData),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				updateTable(data);
			},
			failure: function (errMsg) {
				console.log("error", errMsg);
			}
		});

	});

	$("#delete-item").click(function () {
		var rowId = $(this).attr("del-row");
		var owner = $("#owners-list").find(":selected").text();
		var tableRow = $("th").filter(function () {
			return $(this).text() == rowId;
		}).closest("tr");

		var purchaseData = {};
		var index = 0;
		$(tableRow).children().each(function () {
			if (index !== 0) {
				var value = $(this).text();
				var field = purchaseItemFields[index];
				purchaseData[field] = value;
			}
			index++;
		});

		$.ajax({
			type: "DELETE",
			url: "/purchase/" + owner,
			data: JSON.stringify(purchaseData),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				updateTable(data);
			},
			failure: function (errMsg) {
				console.log("error", errMsg);
			}
		});
	});

	$('table tbody').on("click", "tr", function () {
		var index = $(this).find("th");
		$("#delete-item").attr("del-row", $(index).text());
		$("tbody  > tr").each(function () {
			$(this).removeClass("selected");
		});
		$(this).addClass("selected");
	});

});

var purchaseItemFields = ["skip", "name", "quantity"];

function updateTable(data) {
	var tableBody = $("tbody");
	tableBody.empty();
	var items = data.items;
	for (var i = 0; i < items.length; i++) {
		tableBody.append("<tr><th scope='row'>" + i + "</th><td>" + items[i].name + "</td><td>" + items[i].quantity + "</td></tr>");
	}
}